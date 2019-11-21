/**
Copyright © 2019  Atos Spain SA. All rights reserved.
This file is part of SEAL Configuration Manager (SEAL ConfMngr).
SEAL ConfMngr is free software: you can redistribute it and/or modify it under the terms of EUPL 1.2.
THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT ANY WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT, 
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
DAMAGES OR OTHER LIABILITY, WHETHER IN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
See README file for the full disclaimer information and LICENSE file for full license information in the project root.
*/
package eu.seal.cm.rest_api.domain;


import java.util.Set;

import org.springframework.validation.annotation.Validated;

import java.util.Map;
import java.util.Collection;
import java.util.HashMap;

//import org.springframework.validation.annotation.Validated;




/**
 * AttributeMappingList
 * TODO
 */

@Validated
public class AttributeMappingList extends HashMap<String,String>  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	  public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("class AttributeMappingList {\n");
	    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
	    sb.append("}");
	    return sb.toString();
	  }
	
	/**
	   * Convert the given object to string with each line indented by 4 spaces
	   * (except the first line).
	   */
	  private String toIndentedString(java.lang.Object o) {
	    if (o == null) {
	      return "null";
	    }
	    return o.toString().replace("\n", "\n    ");
	  }
	
}

//public class AttributeMappingList implements Map<String,String>  {
//
//
//
//@Override
//public int size() {
//	// TODO Auto-generated method stub
//	return 0;
//}
//
//@Override
//public boolean isEmpty() {
//	// TODO Auto-generated method stub
//	return false;
//}
//
//@Override
//public boolean containsKey(Object key) {
//	// TODO Auto-generated method stub
//	return false;
//}
//
//@Override
//public boolean containsValue(Object value) {
//	// TODO Auto-generated method stub
//	return false;
//}
//
//@Override
//public String get(Object key) {
//	// TODO Auto-generated method stub
//	return null;
//}
//
//@Override
//public String put(String key, String value) {
//	// TODO Auto-generated method stub
//	return null;
//}
//
//@Override
//public String remove(Object key) {
//	// TODO Auto-generated method stub
//	return null;
//}
//
//@Override
//public void putAll(Map<? extends String, ? extends String> m) {
//	// TODO Auto-generated method stub
//	
//}
//
//@Override
//public void clear() {
//	// TODO Auto-generated method stub
//	
//}
//
//@Override
//public Set<String> keySet() {
//	// TODO Auto-generated method stub
//	return null;
//}
//
//@Override
//public Collection<String> values() {
//	// TODO Auto-generated method stub
//	return null;
//}
//
//@Override
//public Set<Entry<String, String>> entrySet() {
//	// TODO Auto-generated method stub
//	return null;
//}
//}

