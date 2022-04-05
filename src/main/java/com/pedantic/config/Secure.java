package com.pedantic.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;


import javax.ws.rs.NameBinding;

//NameBinding is a simple annotation that used to relate a resource with a given filter
//eg. associating SecurityFilter filter with any resource that has this annotation "employees" resource is related
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE, METHOD})
public @interface Secure {

}
