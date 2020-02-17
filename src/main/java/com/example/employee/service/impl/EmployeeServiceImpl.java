package com.example.employee.service.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.example.employee.Employee;
import com.example.employee.service.EmployeeSalaryRange;
import com.example.employee.service.EmployeeService;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.CsvToBean;
import au.com.bytecode.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

@Service
@CacheConfig(cacheNames ="cacheStore") 
public class EmployeeServiceImpl implements EmployeeService{

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);
	
	/**
	 * The default column delimiter.
	 */
	public static final char DEFAULT_COL_DELIMETER = ',';

	/**
	 * The default text qualifier.
	 */
	public static final char DEFAULT_TEXT_QUALIFIER = '"';
	
	static List<Employee> employees = new ArrayList<Employee>();
		
	
	private CSVReader csvReader;

	private char colDelimeter = DEFAULT_COL_DELIMETER;

	private char textQualifier = DEFAULT_TEXT_QUALIFIER;
	
	@Autowired
	private ResourceLoader  resourceLoader;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Override
	@PostConstruct
	public void init(){
		
		Resource resource = resourceLoader.getResource("classpath:"+"employee.csv");
		
		try {
			final FileInputStream fis = new FileInputStream(resource.getURI().getPath());
			final InputStreamReader isr = new InputStreamReader(fis);
			csvReader = new CSVReader(new BufferedReader(isr), colDelimeter, textQualifier);
			
			// Hashmap to map CSV data to  
	        // Bean attributes. 
	        Map<String, String> mapping = new 
	                      HashMap<String, String>(); 
	        mapping.put("employeeID", "EmployeeID"); 
	        mapping.put("employeeName", "EmployeeName"); 
	        mapping.put("title", "Title"); 
	        mapping.put("businessUnit", "BusinessUnit"); 
	        mapping.put("place", "Place"); 
	        mapping.put("supervisorID", "SupervisorID"); 
	        mapping.put("competencies", "Competencies"); 
	        mapping.put("salary", "Salary"); 
	  
	        HeaderColumnNameTranslateMappingStrategy<Employee> strategy = 
	                new HeaderColumnNameTranslateMappingStrategy<Employee>(); 
	           strategy.setType(Employee.class); 
	           strategy.setColumnMapping(mapping); 
	        
	        CsvToBean csvToBean = new CsvToBean(); 
	           
	        // call the parse method of CsvToBean 
	        // pass strategy, csvReader to parse method 
	        List<Employee> employeeList = csvToBean.parse(strategy, csvReader); 
	          
		
			//Create a cache manager
	        CacheManager cm = CacheManager.getInstance();
	        
	        Cache cache = cacheManager.getCache("cacheStore");
	        for(Employee employee: employeeList){
	        	cache.put(new Element(employee.getEmployeeID(), employee));	        	
	        }	        
	              
	        
	        employees.addAll(employeeList);
	        
		} catch (FileNotFoundException e) {
			//TODO
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			//TODO
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	@Cacheable(cacheNames = "cacheStore", key = "#place, #percentage")
	public List<Employee> updateEmployeesByPlace(String place, double percentage){
		 employees.stream().filter(e -> e.getPlace().equalsIgnoreCase(place)).parallel().forEach(e -> e.salaryIncrement(percentage));         
         return employees;        			
	}
	
	@Override
	@Cacheable(cacheNames = "cacheStore", key = "#supervisorId")
	public List<Employee> getEmployeesBySupervisor(String supervisorId){
	
		 Function<List<Employee>, Map<String, ArrayList<Employee>>> test = (x) -> {

             Map<String, ArrayList<Employee>> result = new HashMap<String, ArrayList<Employee>>();

             ArrayList<Employee> employees = null;

             for (Employee employee : x) {

                   String employeeId = employee.getEmployeeID();

                   for (Employee checkEmp : x) {

                          if (employeeId.equals(checkEmp.getSupervisorID())) {

                                 if (result.containsKey(employeeId)) {

                                        ArrayList<Employee> tempEmp = result.get(employeeId);

                                        tempEmp.add(checkEmp);

                                        result.put(employeeId, tempEmp);

                                 } else {

                                        employees = new ArrayList<Employee>();

                                        employees.add(checkEmp);

                                        result.put(employeeId, employees);

                                 }



                          }

                   }

             }

             return result;

      };
      
      ArrayList<Employee> empls = test.apply(employees).get(supervisorId);
      		return empls;		
	}
	
	@Override
	@Cacheable(cacheNames = "cacheStore", key = "#group, #groupValue")
	public double getTotalSalary(String group, String groupValue) {
        if(group.equalsIgnoreCase("BU")){
        	return employees
        			.stream()
        			.filter(e -> e.getBusinessUnit().equalsIgnoreCase(groupValue))
        			.mapToDouble(Employee::getSalary).sum();
        } else if(group.equalsIgnoreCase("supervisor")){
        	return employees
			.stream()
			.filter(e -> e.getSupervisorID().equalsIgnoreCase(groupValue))
			.mapToDouble(Employee::getSalary).sum();        	
        } else if(group.equalsIgnoreCase("place")){
        	return employees
        			.stream()
        			.filter(e -> e.getPlace().equalsIgnoreCase(groupValue))
        			.mapToDouble(Employee::getSalary).sum();
        }else{
        	return 0;
        }
        	
        
	}
	
	@Override
	@Cacheable(cacheNames = "cacheStore", key = "#place")
	public List<Employee> getEmployeesByPlace(String place){		
        return employees
        		.stream()
        		.filter(e -> e.getPlace().equalsIgnoreCase(place))
        		.collect(Collectors.toList());
		
	}
	
	@Override
	@Cacheable(cacheNames = "cacheStore",  key = "#title")
	public EmployeeSalaryRange getEmployeeSalaryRangeByTitle(String title){
		double min = employees.stream().filter(e -> e.getTitle().equalsIgnoreCase(title))
      	      .min(Comparator.comparing(Employee::getSalary))
      	      .orElseThrow(NoSuchElementException::new).getSalary();
		double max = employees.stream().filter(e -> e.getTitle().equalsIgnoreCase(title))
      	      .max(Comparator.comparing(Employee::getSalary))
      	      .orElseThrow(NoSuchElementException::new).getSalary();
		
		return  new EmployeeSalaryRange(title, min, max, Timestamp.valueOf(LocalDateTime.now())); 
                		
	}

}
