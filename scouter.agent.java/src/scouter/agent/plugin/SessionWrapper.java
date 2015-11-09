/*
 *  Copyright 2015 the original author or authors. 
 *  @https://github.com/scouter-project/scouter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. 
 */
package scouter.agent.plugin;

import java.util.Enumeration;

import scouter.agent.Logger;

public class SessionWrapper extends Wrapper {

	private Object reqObject;

	private static java.lang.reflect.Method getAttribute;
	private static java.lang.reflect.Method getAttributeNames;

	private static boolean enabled = true;

	public SessionWrapper(Object req) {
		if (req == null) {
			enabled = false;
		}
		reqObject = req;
	}

	public String getAttribute(String key) {
		if (enabled == false)
			return null;
		try {
			if (getAttribute == null) {
				getAttribute = this.reqObject.getClass().getMethod("getAttribute", arg_c_s);
			}
			return (String) getAttribute.invoke(reqObject, new Object[] { key });
		} catch (Throwable e) {
			enabled = false;
			Logger.println("A176", e);
			return null;
		}
	}

	public Enumeration getAttributeNames() {
		if (enabled == false)
			return null;
		try {
			if (getAttributeNames == null) {
				getAttributeNames = this.reqObject.getClass().getMethod("getAttributeNames", arg_c);
			}
			return (Enumeration) getAttributeNames.invoke(reqObject, arg_o);
		} catch (Throwable e) {
			enabled = false;
			Logger.println("A177", e);
			return null;
		}
	}
}