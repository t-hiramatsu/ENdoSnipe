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
package jp.co.acroquest.endosnipe.javelin.bean;

import jp.co.acroquest.endosnipe.javelin.CallTreeNode;
import junit.framework.TestCase;

/**
 * @author iida
 *
 */
public class ComponentTest extends TestCase
{
    /** �N���X���B */
    private static final String CLASS_NAME = "ClassName";
    
    /** �v���Z�X���B */
    private static final String PROCESS_NAME = "ProcessName";
    
    /** ���v���s���Ԃ�ݒ肷�鎞�ɕK�v��CallTreeNode�I�u�W�F�N�g�B */
    private static CallTreeNode callTreeNode__ = new CallTreeNode();
    
    /**
     * �w�肳�ꂽ���\�b�h���ƍ��v���s���Ԃ�����Invocation�𐶐����A�Ԃ��܂��B<br />
     * 
     * @param methodName ���\�b�h��
     * @param totalTime ���v���s����
     * @return �V����Invocation�I�u�W�F�N�g
     */
    private Invocation createInvocation(String methodName, int totalTime)
    {
        Invocation invocation = new Invocation(PROCESS_NAME, CLASS_NAME, methodName,
                                               Invocation.THRESHOLD_NOT_SPECIFIED);
        invocation.addInterval(callTreeNode__, totalTime, totalTime, totalTime);
        return invocation;
    }
    
    /**
     * Invocation���ő吔�܂œ��ꂽ��A�V����Invocation�������A�Â��v�f�̍폜���m�F���܂��B<br />
     * �폜�����̂́A�ŏ���Invocation�ł��B<br />
     */
    public void testAddAndDeleteOldestInvocation_RemoveFirstInvocation()
    {
        Component component = new Component(CLASS_NAME);
        final String removedMethodName = "MethodName1";
        
        // javelin.record.invocation.num.max�Ŏw�肵�����ɂȂ�܂ŁAInvocation��������B
        component.addInvocation(createInvocation(removedMethodName, 10));
        for (int count = 2; count <= 1024; count++)
        {
            component.addInvocation(createInvocation("MethodName" + count, 20));
        }
        
        // ����1:�T�C�Y���ő�l�A���폜�\��̗v�f���܂����݂���B
        int size = component.getRecordedInvocationNum();
        Invocation removedInvocation = component.getInvocation(removedMethodName);
        assertEquals(size, 1024);
        assertNotNull(removedInvocation);
        
        // �V����Invocation���AaddAndDeleteOldestInvocation���\�b�h�ŉ�����B
        component.addAndDeleteOldestInvocation(createInvocation("MethodName1025", 20));
        
        // ����2�F�T�C�Y���ő�l�A���폜�\��̗v�f���폜����Ă���B
        size = component.getRecordedInvocationNum();
        removedInvocation = component.getInvocation(removedMethodName);
        assertEquals(size, 1024);
        assertNull(removedInvocation);
    }
    
    /**
     * Invocation���ő吔�܂œ��ꂽ��A�V����Invocation�������A�Â��v�f�̍폜���m�F���܂��B<br />
     * �폜�����̂́A�r����Invocation�ł��B<br />
     */
    public void testAddAndDeleteOldestInvocation_RemoveMiddleInvocation()
    {
        Component component = new Component(CLASS_NAME);
        final String removedMethodName = "MethodName512";
        
        // javelin.record.invocation.num.max�Ŏw�肵�����ɂȂ�܂ŁAInvocation��������B
        for (int count = 1; count <= 511; count++)
        {
            component.addInvocation(createInvocation("MethodName" + count, 20));
        }
        component.addInvocation(createInvocation(removedMethodName, 10));
        for (int count = 513; count <= 1024; count++)
        {
            component.addInvocation(createInvocation("MethodName" + count, 20));
        }
        
        // ����1:�T�C�Y���ő�l�A���폜�\��̗v�f���܂����݂���B
        int size = component.getRecordedInvocationNum();
        Invocation removedInvocation = component.getInvocation(removedMethodName);
        assertEquals(size, 1024);
        assertNotNull(removedInvocation);
        
        // �V����Invocation���AaddAndDeleteOldestInvocation���\�b�h�ŉ�����B
        component.addAndDeleteOldestInvocation(createInvocation("MethodName1025", 20));
        
        // ����2�F�T�C�Y���ő�l�A���폜�\��̗v�f���폜����Ă���B
        size = component.getRecordedInvocationNum();
        removedInvocation = component.getInvocation(removedMethodName);
        assertEquals(size, 1024);
        assertNull(removedInvocation);
    }
    
    /**
     * Invocation���ő吔�܂œ��ꂽ��A�V����Invocation�������A�Â��v�f�̍폜���m�F���܂��B<br />
     * �폜�����̂́A��ԍŌ��Invocation�ł��B<br />
     */
    public void testAddAndDeleteOldestInvocation_RemoveLastInvocation()
    {
        Component component = new Component(CLASS_NAME);
        final String removedMethodName = "MethodName1024";
        
        // javelin.record.invocation.num.max�Ŏw�肵�����ɂȂ�܂ŁAInvocation��������B
        for (int count = 1; count <= 1023; count++)
        {
            component.addInvocation(createInvocation("MethodName" + count, 20));
        }
        component.addInvocation(createInvocation(removedMethodName, 10));
        
        // ����1:�T�C�Y���ő�l�A���폜�\��̗v�f���܂����݂���B
        int size = component.getRecordedInvocationNum();
        Invocation removedInvocation = component.getInvocation(removedMethodName);
        assertEquals(size, 1024);
        assertNotNull(removedInvocation);
        
        // �V����Invocation���AaddAndDeleteOldestInvocation���\�b�h�ŉ�����B
        component.addAndDeleteOldestInvocation(createInvocation("MethodName1025", 20));
        
        // ����2�F�T�C�Y���ő�l�A���폜�\��̗v�f���폜����Ă���B
        size = component.getRecordedInvocationNum();
        removedInvocation = component.getInvocation(removedMethodName);
        assertEquals(size, 1024);
        assertNull(removedInvocation);
    }
    
    /**
     * Invocation���ő吔�܂œ��ꂽ��A�V����Invocation�������A�Â��v�f�̍폜���m�F���܂��B<br />
     * �������A�S�Ă�Invocation�̍��v���s���Ԃ������ł��B<br />
     */
    public void testAddAndDeleteOldestInvocation_SameTotalTimeInvocations()
    {
        Component component = new Component(CLASS_NAME);
        final String removedMethodName = "MethodName1";
        
        // javelin.record.invocation.num.max�Ŏw�肵�����ɂȂ�܂ŁAInvocation��������B
        component.addInvocation(createInvocation(removedMethodName, 20));
        for (int count = 2; count <= 1024; count++)
        {
            component.addInvocation(createInvocation("MethodName" + count, 20));
        }
        
        // ����1:�T�C�Y���ő�l�A���폜�\��̗v�f���܂����݂���B
        int size = component.getRecordedInvocationNum();
        Invocation removedInvocation = component.getInvocation(removedMethodName);
        assertEquals(size, 1024);
        assertNotNull(removedInvocation);
        
        // �V����Invocation���AaddAndDeleteOldestInvocation���\�b�h�ŉ�����B
        component.addAndDeleteOldestInvocation(createInvocation("MethodName1025", 20));
        
        // ����2�F�T�C�Y���ő�l�A���폜�\��̗v�f���폜����Ă���B
        size = component.getRecordedInvocationNum();
        removedInvocation = component.getInvocation(removedMethodName);
        assertEquals(size, 1024);
        assertNull(removedInvocation);
    }
}
