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
package jp.co.acroquest.endosnipe.collector.config;

import jp.co.acroquest.endosnipe.collector.exception.InitializeException;

/**
 * Javelin �G�[�W�F���g�ւ̐ڑ��ݒ��ێ�����N���X�ł��B<br />
 * 
 * @author y-komori
 */
public class AgentSetting
{
    /** �ڑ���|�[�g�ԍ��̃f�t�H���g�l */
    public static final int        DEF_PORT                              = 18000;

    /** BottleneckEye ����̐ڑ��҂��󂯃|�[�g�ԍ��̃f�t�H���g�l */
    public static final int        DEF_ACCEPT_PORT                       = DEF_PORT + 10000;

    /** Javelin���O�̍ő�~�ϊ��Ԃ̃f�t�H���g�l */
    public static final String     DEF_JVN_LOG_STRAGE_PERIOD             = "7d";

    /** �v���f�[�^�̍ő�~�ϊ��Ԃ̃f�t�H���g�l */
    public static final String     DEF_MEASUREMENT_JVN_LOG_STRAGE_PERIOD = "7d";

    public static final String     NONE                                  = "NONE";

    public static final int        DEF_PERIOD                            = 7;

    public static final PeriodUnit DEF_PERIOD_UNIT                       = PeriodUnit.DAY;

    /** 1���̎��Ԑ� */
    public static final int        HOURS_PER_DAY                         = 24;

    /** �G�[�W�F���g ID */
    public int                      agentId;

    /** �f�[�^�x�[�X�� */
    public String                   databaseName;

    /** �ڑ���z�X�g�� */
    public String                   hostName;

    /** �ڑ���|�[�g�ԍ� */
    public int                      port                                  = DEF_PORT;

    /** BottleneckEye ����̐ڑ��҂��󂯃|�[�g�ԍ� */
    public int                      acceptPort                            = DEF_ACCEPT_PORT;

    /** Javelin���O�̍ő�~�ϊ��� */
    public String                   jvnLogStragePeriod                    =
                                                                            DEF_JVN_LOG_STRAGE_PERIOD;

    /** �v���f�[�^�̍ő�~�ό��� */
    public String                   measureStragePeriod                   =
                                                                            DEF_MEASUREMENT_JVN_LOG_STRAGE_PERIOD;

    /** Javelin���O�̒~�ϊ���(�P��) */
    private PeriodUnit              jvnLogStragePeriodUnit_;

    /** �v���f�[�^�̒~�ϊ���(�P��) */
    private PeriodUnit              measureStragePeriodUnit_;

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "Host:" + hostName + " Port:" + port;
    }

    /**
     * Javelin���O�̒~�ϊ���(�l)���擾���܂��B<br />
     * 
     * @return Javelin���O�̒~�ϊ���(�l)
     * @throws InitializeException �p�����[�^�̏������Ɏ��s�����ꍇ
     */
    public int getJavelinRotatePeriod()
        throws InitializeException
    {
        if (NONE.equals(this.jvnLogStragePeriod))
        {
            this.jvnLogStragePeriodUnit_ = DEF_PERIOD_UNIT;
            return 0;
        }
        if (this.jvnLogStragePeriod == null || this.jvnLogStragePeriod.length() < 1)
        {
            this.jvnLogStragePeriodUnit_ = DEF_PERIOD_UNIT;
            return DEF_PERIOD;
        }
        String storagePriodStr =
                                 this.jvnLogStragePeriod.substring(
                                                                   0,
                                                                   this.jvnLogStragePeriod.length() - 1);
        int storagePeriod = DEF_PERIOD;
        try
        {
            storagePeriod = Integer.parseInt(storagePriodStr);
            String storagePriodUnitStr =
                                         this.jvnLogStragePeriod.substring(this.jvnLogStragePeriod.length() - 1);
            if ("d".equals(storagePriodUnitStr))
            {
                this.jvnLogStragePeriodUnit_ = PeriodUnit.DAY;
            }
            else if ("m".equals(storagePriodUnitStr))
            {
                this.jvnLogStragePeriodUnit_ = PeriodUnit.MONTH;
            }
            else if ("h".equals(storagePriodUnitStr))
            {
                // ���ԒP�ʂŋL�q����Ă���ꍇ�́A���t�P�ʂɐ؂�グ��
                this.jvnLogStragePeriodUnit_ = PeriodUnit.DAY;
                storagePeriod = (storagePeriod + HOURS_PER_DAY - 1) / HOURS_PER_DAY;
            }
            else
            {
                throw new InitializeException("Invalid Unit.");
            }
        }
        catch (NumberFormatException ex)
        {
            throw new InitializeException(ex);
        }
        return storagePeriod;
    }

    /**
     * Javelin���O�̒~�ϊ���(�P��)���擾���܂��B<br />
     * 
     * @return Javelin���O�̒~�ϊ���(�P��)
     */
    public PeriodUnit getJavelinRotatePeriodUnit()
    {
        return this.jvnLogStragePeriodUnit_;
    }

    /**
     * �v���f�[�^�̒~�ϊ���(�l)���擾���܂��B<br />
     * 
     * @return �v���f�[�^�̒~�ϊ���(�l)
     * @throws InitializeException �p�����[�^�̏������Ɏ��s�����ꍇ
     */
    public int getMeasurementRotatePeriod()
        throws InitializeException
    {
        if (NONE.equals(this.measureStragePeriod))
        {
            this.measureStragePeriodUnit_ = DEF_PERIOD_UNIT;
            return 0;
        }
        if (this.measureStragePeriod == null || this.measureStragePeriod.length() < 1)
        {
            this.measureStragePeriodUnit_ = DEF_PERIOD_UNIT;
            return DEF_PERIOD;
        }
        String storagePriodStr =
                                 this.measureStragePeriod.substring(
                                                                    0,
                                                                    this.measureStragePeriod.length() - 1);
        int storagePeriod = DEF_PERIOD;
        try
        {
            storagePeriod = Integer.parseInt(storagePriodStr);
            String storagePriodUnitStr =
                                         this.measureStragePeriod.substring(this.measureStragePeriod.length() - 1);
            if ("d".equals(storagePriodUnitStr))
            {
                this.measureStragePeriodUnit_ = PeriodUnit.DAY;
            }
            else if ("m".equals(storagePriodUnitStr))
            {
                this.measureStragePeriodUnit_ = PeriodUnit.MONTH;
            }
            else if ("h".equals(storagePriodUnitStr))
            {
                // ���ԒP�ʂŋL�q����Ă���ꍇ�́A���t�P�ʂɐ؂�グ��
                this.measureStragePeriodUnit_ = PeriodUnit.DAY;
                storagePeriod = (storagePeriod + HOURS_PER_DAY - 1) / HOURS_PER_DAY;
            }
            else
            {
                throw new InitializeException("Invalid Unit.");
            }
        }
        catch (NumberFormatException ex)
        {
            throw new InitializeException(ex);
        }
        return storagePeriod;
    }

    /**
     * �v���f�[�^�̒~�ϊ���(�P��)���擾���܂��B<br />
     * 
     * @return �v���f�[�^�̒~�ϊ���(�P��)
     */
    public PeriodUnit getMeasurementRotatePeriodUnit()
    {
        return this.measureStragePeriodUnit_;
    }
}
