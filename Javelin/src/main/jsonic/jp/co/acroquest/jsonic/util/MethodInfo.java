/***************************************import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
(MIT)
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MethodInfo implements Comparable<MethodInfo> {
	private Class<?> beanClass;
	private String name;
	private boolean isStatic;
	
	List<Method> methods = new ArrayList<Method>();
	
	public MethodInfo(Class<?> beanClass, String name, Collection<Method> methods, boolean isStatic) {
		this.beanClass = beanClass;
		this.name = name;
		this.isStatic = isStatic;
		if (methods != null) this.methods.addAll(methods);
	}
	
	public Class<?> getBeanClass() {
		return beanClass;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isStatic() {
		return isStatic;
	}
	
	public Object invoke(Object instance, Object... args) {
		Method method = findMethod(args);
		try {
			return method.invoke(instance, args);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
	
	public int compareTo(MethodInfo method) {
		if (!beanClass.equals(method.beanClass)) {
			return beanClass.getName().compareTo(method.beanClass.getName());			
		} else {
			return name.compareTo(method.name);
		}
	}
	
	public Method findMethod(Object... args) {
		Method method = null;
		Class<?>[] types = null;
		
		Method vmethod = null;
		Class<?>[] vtypes = null;		
		
		for (Method cmethod : methods) {
			Class<?>[] cparams = cmethod.getParameterTypes();
			
			if (cmethod.isVarArgs()) {
				if (args.length < cparams.length-1) {
					continue;
				}
				
				if (vmethod == null) {
					Class<?> vtype = cparams[cparams.length-1].getComponentType();
					Class<?>[] tmp = new Class<?>[args.length];
					System.arraycopy(cparams, 0, tmp, 0, cparams.length-1);
					for (int i = cparams.length-1; i < tmp.length; i++) {
						tmp[i] = vtype;
					}
					vmethod = cmethod;
					vtypes = tmp;
				} else {
					int vpoint = BeanInfo.calcurateDistance(vtypes, args);
					int cpoint = BeanInfo.calcurateDistance(cparams, args);
					if (cpoint > vpoint) {
						vmethod = cmethod;
						vtypes = cparams;
					} else if (cpoint == vpoint) {
						cmethod = null;
						cparams = null;
					}
				}
			} else {
				if (args.length != cparams.length) {
					continue;
				}
				
				if (method == null) {
					method = cmethod;
					types = cparams;
				} else {
					int point = BeanInfo.calcurateDistance(types, args);
					int cpoint = BeanInfo.calcurateDistance(cparams, args);
					if (cpoint > point) {
						method = cmethod;
						types = cparams;
					} else if (cpoint == point) {
						cmethod = null;
						cparams = null;
					}
				}
			}			
		}
		
		if (vmethod != null) {
			if (method == null) {
				method = vmethod;
			} else {
				int point = BeanInfo.calcurateDistance(types, args);
				int vpoint = BeanInfo.calcurateDistance(vtypes, args);
				if (vpoint > point) {
					method = vmethod;
				}
			}
		}
		
		if (method == null) {
			throw new IllegalStateException("suitable method is not found: " + name);
		}
		
		return method;		
	}
}
