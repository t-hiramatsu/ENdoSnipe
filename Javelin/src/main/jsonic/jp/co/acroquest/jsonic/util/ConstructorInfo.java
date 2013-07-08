/*******************************************************************************
 * ENdoSnipe 5.0 - (https://github.com/endosnipe)
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) 2012 Acroquest Technology Co.,Ltd.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package jp.co.acroquest.jsonic.util;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConstructorInfo implements Comparable<ConstructorInfo> {
	private Class<?> beanClass;
	
	List<Constructor<?>> constructors = new ArrayList<Constructor<?>>();
	
	public ConstructorInfo(Class<?> beanClass, Collection<Constructor<?>> constructors) {
		this.beanClass = beanClass;
		if (constructors != null) this.constructors.addAll(constructors);
	}
	
	public Class<?> getBeanClass() {
		return beanClass;
	}
	
	public Object newInstance(Object... args) {
		Constructor<?> constructor = findConstructor(args);
		try {
			return constructor.newInstance(args);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
	
	public int compareTo(ConstructorInfo constructor) {
		if (!beanClass.equals(constructor.beanClass)) {
			return beanClass.getName().compareTo(constructor.beanClass.getName());			
		} else {
			return 0;
		}
	}
	
	public Constructor<?> findConstructor(Object... args) {
		Constructor<?> constructor = null;
		Class<?>[] types = null;
		
		Constructor<?> vconstructor = null;
		Class<?>[] vtypes = null;		
		
		for (Constructor<?> cconstructor : constructors) {
			Class<?>[] cparams = cconstructor.getParameterTypes();
			
			if (cconstructor.isVarArgs()) {
				if (args.length < cparams.length-1) {
					continue;
				}
				
				if (vconstructor == null) {
					Class<?> vtype = cparams[cparams.length-1].getComponentType();
					Class<?>[] tmp = new Class<?>[args.length];
					System.arraycopy(cparams, 0, tmp, 0, cparams.length-1);
					for (int i = cparams.length-1; i < tmp.length; i++) {
						tmp[i] = vtype;
					}
					vconstructor = cconstructor;
					vtypes = tmp;
				} else {
					int vpoint = BeanInfo.calcurateDistance(vtypes, args);
					int cpoint = BeanInfo.calcurateDistance(cparams, args);
					if (cpoint > vpoint) {
						vconstructor = cconstructor;
						vtypes = cparams;
					} else if (cpoint == vpoint) {
						cconstructor = null;
						cparams = null;
					}
				}
			} else {
				if (args.length != cparams.length) {
					continue;
				}
				
				if (constructor == null) {
					constructor = cconstructor;
					types = cparams;
				} else {
					int point = BeanInfo.calcurateDistance(types, args);
					int cpoint = BeanInfo.calcurateDistance(cparams, args);
					if (cpoint > point) {
						constructor = cconstructor;
						types = cparams;
					} else if (cpoint == point) {
						cconstructor = null;
						cparams = null;
					}
				}
			}			
		}
		
		if (vconstructor != null) {
			if (constructor == null) {
				constructor = vconstructor;
			} else {
				int point = BeanInfo.calcurateDistance(types, args);
				int vpoint = BeanInfo.calcurateDistance(vtypes, args);
				if (vpoint > point) {
					constructor = vconstructor;
				}
			}
		}
		
		if (constructor == null) {
			throw new IllegalStateException("suitable constructor is not found.");
		}
		
		return constructor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((beanClass == null) ? 0 : beanClass.hashCode());
		result = prime * result
				+ ((constructors == null) ? 0 : constructors.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConstructorInfo other = (ConstructorInfo) obj;
		if (beanClass == null) {
			if (other.beanClass != null)
				return false;
		} else if (!beanClass.equals(other.beanClass))
			return false;
		if (constructors == null) {
			if (other.constructors != null)
				return false;
		} else if (!constructors.equals(other.constructors))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ConstructorInfo [beanClass=" + beanClass
			+ ", constructors=" + constructors
			+ "]";
	}
}
