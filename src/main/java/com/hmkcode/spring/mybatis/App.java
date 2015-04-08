package com.hmkcode.spring.mybatis;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.hmkcode.spring.mybatis.service.Service;
import com.hmkcode.spring.mybatis.vo.Person;

public class App {
	private static Log log = LogFactory.getLog(App.class);

	public static void main(String[] args) {
    	AnnotationConfigApplicationContext ctx = null;
    	try {
    		ctx = new AnnotationConfigApplicationContext();
    		ctx.register(AppConfig.class);
    		ctx.refresh();
    		exec(ctx);
    	} catch ( Exception e ) {
    		e.printStackTrace();
    	} finally {
    		if ( ctx != null ) {
    			ctx.close();
    		}
    	}
	}
	
	private static void exec(ApplicationContext cxt) {

		Service service = (Service) cxt.getBean("service");

		log.info("Running App...");

		System.out.println("List<Person> persons = service.selectAllPerson()");
		List<Person> persons = service.selectAllPerson();
		for ( Person ele : persons ) {
			System.out.println("-> " + ele);
		}
		System.out.println();

		System.out.println("Person person = service.selectPerson(2)");
		Person person = service.selectPerson(2);
		System.out.println("-> " + person + "\n");

		System.out.println("service.insertPerson(person)");
		person.setName("Inserted person");
		service.insertPerson(person);
		System.out.println("-> inserted..." + "\n");

		System.out.println("List<Person> persons = service.selectAllPerson()");
		persons = service.selectAllPerson();
		for ( Person ele : persons ) {
			System.out.println("-> " + ele);
		}
		System.out.println();
	}
}
