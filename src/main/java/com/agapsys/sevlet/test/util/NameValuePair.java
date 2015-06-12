/*
 * Copyright 2015 Agapsys Tecnologia Ltda-ME.
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

package com.agapsys.sevlet.test.util;

public class NameValuePair extends Pair<String, String> {

	/**
	 * @throws IllegalArgumentException if ((name == null || name.isEmpty()) || (value == null))
	 */
	public NameValuePair(String name, String value) throws IllegalArgumentException {
		super(name, value);
		
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("Null/Empty name");
		
		if (value == null)
			throw new IllegalArgumentException("Null value");
	}

	public String getName() {
		return super.getFirst();
	}

	public String getValue() {
		return super.getSecond();
	}
}
