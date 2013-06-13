/**
 * 
 */
package com.googlecode.jsonschema2pojo;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.annotations.SerializedName;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;

/**
 * Annotates generated Java types using the Gson. The annotations used here are
 * most useful when the JSON fields have characters (like underscores) that are
 * poorly suited for beans. By using the {@link SerializedName} annotation, we
 * are able to preserve the original format. Use this in conjunction with
 * {@link SchemaMapper#getPropertyWordDelimiters} to filter out underscores or
 * other unwanted delimiters but still marshal/unmarshal the same content.
 */
public class GsonAnnotator extends AbstractAnnotator {
    
    @Override
    public void propertyField(JFieldVar field, JDefinedClass clazz,
	    String propertyName, JsonNode propertyNode) {
	field.annotate(SerializedName.class).param("value", propertyName);
    }
    
}
