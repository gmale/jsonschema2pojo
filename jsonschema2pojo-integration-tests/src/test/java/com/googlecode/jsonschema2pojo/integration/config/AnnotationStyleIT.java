/**
 * Copyright © 2010-2013 Nokia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.googlecode.jsonschema2pojo.integration.config;

import static com.googlecode.jsonschema2pojo.integration.util.CodeGenerationHelper.compile;
import static com.googlecode.jsonschema2pojo.integration.util.CodeGenerationHelper.config;
import static com.googlecode.jsonschema2pojo.integration.util.CodeGenerationHelper.generate;
import static com.googlecode.jsonschema2pojo.integration.util.CodeGenerationHelper.generateAndCompile;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.lang.reflect.Method;

import org.apache.maven.plugin.MojoExecutionException;
import org.hamcrest.Matcher;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.googlecode.jsonschema2pojo.integration.util.FileSearchMatcher;

public class AnnotationStyleIT {

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void defaultAnnotationStyeIsJackson2() throws ClassNotFoundException, SecurityException, NoSuchMethodException {

        ClassLoader resultsClassLoader = generateAndCompile("/schema/properties/primitiveProperties.json", "com.example");

        Class generatedType = resultsClassLoader.loadClass("com.example.PrimitiveProperties");

        Method getter = generatedType.getMethod("getA");

        assertThat(generatedType.getAnnotation(JsonPropertyOrder.class), is(notNullValue()));
        assertThat(generatedType.getAnnotation(JsonInclude.class), is(notNullValue()));
        assertThat(getter.getAnnotation(JsonProperty.class), is(notNullValue()));
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void annotationStyleJacksonProducesJackson2Annotations() throws ClassNotFoundException, SecurityException, NoSuchMethodException {

        ClassLoader resultsClassLoader = generateAndCompile("/schema/properties/primitiveProperties.json", "com.example",
                config("annotationStyle", "jackson"));

        Class generatedType = resultsClassLoader.loadClass("com.example.PrimitiveProperties");

        Method getter = generatedType.getMethod("getA");

        assertThat(generatedType.getAnnotation(JsonPropertyOrder.class), is(notNullValue()));
        assertThat(generatedType.getAnnotation(JsonInclude.class), is(notNullValue()));
        assertThat(getter.getAnnotation(JsonProperty.class), is(notNullValue()));
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void annotationStyleJackson2ProducesJackson2Annotations() throws ClassNotFoundException, SecurityException, NoSuchMethodException {

        File generatedOutputDirectory = generate("/schema/properties/primitiveProperties.json", "com.example",
                config("annotationStyle", "jackson2"));

        assertThat(generatedOutputDirectory, not(containsText("org.codehaus.jackson")));
        assertThat(generatedOutputDirectory, containsText("com.fasterxml.jackson"));

        ClassLoader resultsClassLoader = compile(generatedOutputDirectory);

        Class generatedType = resultsClassLoader.loadClass("com.example.PrimitiveProperties");

        Method getter = generatedType.getMethod("getA");

        assertThat(generatedType.getAnnotation(JsonPropertyOrder.class), is(notNullValue()));
        assertThat(generatedType.getAnnotation(JsonInclude.class), is(notNullValue()));
        assertThat(getter.getAnnotation(JsonProperty.class), is(notNullValue()));
    }
    
    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void annotationStyleGsonProducesGsonAnnotations() throws ClassNotFoundException, SecurityException, NoSuchMethodException, NoSuchFieldException {
	
	File generatedOutputDirectory = generate("/schema/properties/primitiveProperties.json", "com.example",
		config("annotationStyle", "gson"));
	
	assertThat(generatedOutputDirectory, not(containsText("org.codehaus.jackson")));
	assertThat(generatedOutputDirectory, not(containsText("com.fasterxml.jackson")));
	assertThat(generatedOutputDirectory, containsText("com.google.gson"));
	assertThat(generatedOutputDirectory, containsText("@SerializedName"));
	
	ClassLoader resultsClassLoader = compile(generatedOutputDirectory);
	
	Class generatedType = resultsClassLoader.loadClass("com.example.PrimitiveProperties");
	Method getter = generatedType.getMethod("getA");
	
	assertThat(generatedType.getAnnotation(JsonPropertyOrder.class), is(nullValue()));
	assertThat(generatedType.getAnnotation(JsonInclude.class), is(nullValue()));
	assertThat(getter.getAnnotation(JsonProperty.class), is(nullValue()));
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void annotationStyleJackson1ProducesJackson1Annotations() throws ClassNotFoundException, SecurityException, NoSuchMethodException {

        File generatedOutputDirectory = generate("/schema/properties/primitiveProperties.json", "com.example",
                config("annotationStyle", "jackson1"));

        assertThat(generatedOutputDirectory, not(containsText("com.fasterxml.jackson")));
        assertThat(generatedOutputDirectory, containsText("org.codehaus.jackson"));

        ClassLoader resultsClassLoader = compile(generatedOutputDirectory);

        Class generatedType = resultsClassLoader.loadClass("com.example.PrimitiveProperties");

        Method getter = generatedType.getMethod("getA");

        assertThat(generatedType.getAnnotation(org.codehaus.jackson.annotate.JsonPropertyOrder.class), is(notNullValue()));
        assertThat(generatedType.getAnnotation(org.codehaus.jackson.map.annotate.JsonSerialize.class), is(notNullValue()));
        assertThat(getter.getAnnotation(org.codehaus.jackson.annotate.JsonProperty.class), is(notNullValue()));
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void annotationStyleNoneProducesNoAnnotations() throws ClassNotFoundException, SecurityException, NoSuchMethodException {

        ClassLoader resultsClassLoader = generateAndCompile("/schema/properties/primitiveProperties.json", "com.example",
                config("annotationStyle", "none"));

        Class generatedType = resultsClassLoader.loadClass("com.example.PrimitiveProperties");

        Method getter = generatedType.getMethod("getA");

        assertThat(generatedType.getAnnotation(JsonPropertyOrder.class), is(nullValue()));
        assertThat(generatedType.getAnnotation(JsonSerialize.class), is(nullValue()));
        assertThat(getter.getAnnotation(JsonProperty.class), is(nullValue()));

    }

    @Test
    public void invalidAnnotationStyleCausesMojoException() {

        try {
            generate("/schema/properties/primitiveProperties.json", "com.example", config("annotationStyle", "invalidstyle"));
            fail();
        } catch (RuntimeException e) {
            assertThat(e.getCause(), is(instanceOf(MojoExecutionException.class)));
            assertThat(e.getCause().getMessage(), is(containsString("invalidstyle")));
        }

    }

    private static Matcher<File> containsText(String searchText) {
        return new FileSearchMatcher(searchText);
    }
}
