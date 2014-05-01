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
package jp.co.acroquest.endosnipe.javelin;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jp.co.acroquest.endosnipe.javelin.bean.Component;
import jp.co.acroquest.endosnipe.javelin.bean.ExcludeMonitor;
import jp.co.acroquest.endosnipe.javelin.bean.Invocation;
import jp.co.acroquest.endosnipe.javelin.bean.TripleState;
import jp.co.acroquest.endosnipe.javelin.converter.util.CalledMethodCounter;

/**
 * �R���|�[�l���g���Ǘ�����N���X�B<br />
 *
 * @author acroquest
 */
public class MBeanManager
{
    /** ComponentMBean��o�^�����}�b�v�B */
    private static ConcurrentHashMap<String, Component> mBeanMap__;

    static
    {
        Map<String, Component> deserializedMap = MBeanManagerSerializer.deserialize();
        mBeanMap__ = new ConcurrentHashMap<String, Component>(deserializedMap);

        // �f�V���A���C�Y���� Invocation �̂����A���X�|���X�O���t�̏o�͂� ON �ɂȂ��Ă�����̂��O���t�ɏo�����߂ɁA
        // RootInvocationManager �� Invocation ��o�^����B
        // �܂��A�v���Ώۂ��珜�O���� Invocation �́A�f�V���A���C�Y���� ExcludeMonitor �ɓo�^����B
        for (Component component : mBeanMap__.values())
        {
            for (Invocation invocation : component.getAllInvocation())
            {
                if (invocation.isResponseGraphOutputTarget())
                {
                    RootInvocationManager.addInvocation(invocation);
                }
                if (invocation.getMeasurementTarget() == TripleState.OFF)
                {
                    ExcludeMonitor.addExclude(invocation);
                    ExcludeMonitor.removeTarget(invocation);
                    ExcludeMonitor.removeTargetPreferred(invocation);
                    ExcludeMonitor.removeExcludePreferred(invocation);
                }
            }
        }

        // shutdownHook�̒ǉ�
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run()
            {
                if (mBeanMap__ != null)
                {
                    synchronized (mBeanMap__)
                    {
                        MBeanManagerSerializer.serialize(mBeanMap__);
                    }
                }
            }
        });
    }

    /**
     * �R���X�g���N�^���B�����܂��B<br />
     */
    private MBeanManager()
    {
        // Do nothing.
    }

    /**
     * ���ׂẴR���|�[�l���g��Ԃ��܂��B<br />
     *
     * @return ���ׂẴR���|�[�l���g
     */
    public static Component[] getAllComponents()
    {
        Collection<Component> mBeanMapValues = mBeanMap__.values();
        int size = mBeanMapValues.size();
        Component[] components = mBeanMapValues.toArray(new Component[size]);
        return components;
    }

    /**
     * �w�肳�ꂽ���O�̃R���|�[�l���g��Ԃ��܂��B<br />
     *
     * @param className �擾����R���|�[�l���g�̖��O
     * @return �w�肳�ꂽ�R���|�[�l���g�����݂���ꍇ�̓R���|�[�l���g�I�u�W�F�N�g�A���݂��Ȃ��ꍇ�� <code>null</code>
     */
    public static Component getComponent(final String className)
    {
        return mBeanMap__.get(className);
    }

    /**
     * �w�肳�ꂽ���O�̃R���|�[�l���g��o�^���܂��B<br />
     *
     * @param className �R���|�[�l���g�̖��O
     * @param component �R���|�[�l���g�I�u�W�F�N�g
     * 
     * @return �R���|�[�l���g
     */
    public static Component setComponent(final String className, final Component component)
    {
        return mBeanMap__.putIfAbsent(className, component);
    }

    /**
     * �w�肳�ꂽ���O�̃R���|�[�l���g���폜���܂��B<br />
     *
     * @param className �R���|�[�l���g�̖��O
     */
    public static void removeComponent(final String className)
    {
        mBeanMap__.remove(className);
    }

    /**
     * �R���|�[�l���g���� Invocation �̒l�����Z�b�g���܂��B<br />
     */
    public static void reset()
    {
        for (Component component : mBeanMap__.values())
        {
            synchronized (component)
            {
                component.reset();
            }
        }
        CalledMethodCounter.clear();
    }
}
