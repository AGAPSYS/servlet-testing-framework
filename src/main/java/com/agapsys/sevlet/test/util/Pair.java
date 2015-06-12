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

import java.util.Objects;

/** Represents a pair of objects. */
public class Pair<T1, T2> {
	private final T1 first;
	private final T2 second;
	
	/** Constructor. */
	public Pair(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}
	
	/** @return the first object. */
	protected T1 getFirst() {
		return first;
	}
	
	/** @return the second object. */
	protected T2 getSecond() {
		return second;
	}
	
	@Override
	public String toString() {
		return String.format("[%s, %s]", first.toString(), second.toString());
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 53 * hash + Objects.hashCode(this.first);
		hash = 53 * hash + Objects.hashCode(this.second);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Pair<?, ?> other = (Pair<?, ?>) obj;
		if (!Objects.equals(this.first, other.first)) {
			return false;
		}
		if (!Objects.equals(this.second, other.second)) {
			return false;
		}
		return true;
	}
}