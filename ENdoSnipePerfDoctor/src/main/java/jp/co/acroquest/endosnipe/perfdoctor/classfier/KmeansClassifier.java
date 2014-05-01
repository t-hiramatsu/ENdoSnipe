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
package jp.co.acroquest.endosnipe.perfdoctor.classfier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.co.acroquest.endosnipe.perfdoctor.WarningUnit;

/**
 * �x�����X�g�̒�����A���o�l���L�[�Ƃ��āAKmeans�@�𗘗p���Čx�����X�g�𕪗ނ���B<br />
 * ���ނ���N���X�^����5(CLASSTERNUM)�Ƃ���B
 * ���o�l��������̏ꍇ�A�x�����X�g�̐擪��Ԃ��B<br />
 * 
 * @author fujii
 *
 */
public class KmeansClassifier implements Classifier
{
    /** ���ނ���N���X�^�̍ő吔�B(�����̏ꍇ���̌��ɂȂ�B) */
    private static final int CLASSTERNUM = 5;

    /**
     * {@inheritDoc}
     * 
     * ��:warningUnitList�ɑ΂�����p���N�����̂Œ��ӂ���B
     */
    public List<WarningUnit> classify(final List<WarningUnit> warningUnitList)
    {

        // ���o�l�̃`�F�b�N���s���B
        // ���o�l�����l�łȂ��ꍇ�A���X�g�̐擪�𒊏o���č쐬�������X�g��Ԃ��B
        if (isNumber(warningUnitList) == false)
        {
            List<WarningUnit> resultList = new ArrayList<WarningUnit>();
            resultList.add(warningUnitList.get(0));
            return resultList;
        }
        // ���o�l�����L�[�ɕ��т�����B
        Collections.sort(warningUnitList, new DetectionValueComparator());

        WarningUnitCluster[] oldClusters = new WarningUnitCluster[CLASSTERNUM];
        // �N���X�^�̏��������s���B
        initCluster(warningUnitList, oldClusters);
        while (true)
        {
            // �V�K�N���X�^�̏��������s���B
            WarningUnitCluster[] clusters = new WarningUnitCluster[CLASSTERNUM];
            for (int clusterNum = 0; clusterNum < CLASSTERNUM; clusterNum++)
            {
                clusters[clusterNum] = new WarningUnitCluster();
            }

            // �N���X�^���̕��ϒl�����߂�B
            double[] averages = getAverages(oldClusters);

            // WarningUnit���N���X�^�ɔz�u����B
            for (WarningUnit unit : warningUnitList)
            {
                dispatch(unit, clusters, averages);
            }

            // �O��̃N���X�^�ƍ���̃N���X�^���r���A
            // �����N���X�^�̂Ƃ��Ȃ�A�������I������B
            // �قȂ�N���X�^�̏ꍇ�A�N���X�^�̍Ĕz�u���s���B
            if (compare(oldClusters, clusters))
            {
                break;
            }
            oldClusters = clusters;
        }

        // �N���X�^����WarningUnit�𒊏o���A���X�g���쐬����B
        List<WarningUnit> resultList = clusterToList(oldClusters);
        return resultList;
    }

    /**
     * ���o�l�������ł��邩�`�F�b�N����B
     * @param warningUnitList WarningUnit�̃��X�g
     * @return ���o�l�������ł��邩(�����ł���Ȃ�true)
     */
    private boolean isNumber(final List<WarningUnit> warningUnitList)
    {
        for (WarningUnit unit : warningUnitList)
        {
            Object[] args = unit.getArgs();

            // ���o�l�������ȊO�̃f�[�^�ɂ��ẮAfalse��Ԃ��B
            // �܂��z��̒������A���o�l���i�[�����index�ɋy�΂Ȃ��ꍇ�ɂ�false��Ԃ��B
            if (args.length < PerformanceDoctorFilter.TARGET_VALUE_INDEX + 1)
            {
                return false;
            }
            try
            {
                Double.parseDouble(args[PerformanceDoctorFilter.TARGET_VALUE_INDEX].toString());
            }
            catch (NumberFormatException ex)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * �N���X�^������������B
     * @param warningUnitList WarningUnit�̃��X�g
     * @param clusters �N���X�^
     */
    private void initCluster(final List<WarningUnit> warningUnitList,
            final WarningUnitCluster[] clusters)
    {
        for (int clusterNum = 0; clusterNum < CLASSTERNUM; clusterNum++)
        {
            clusters[clusterNum] = new WarningUnitCluster();
        }

        // �����_���쐬����B
        double cutPoint = (double)warningUnitList.size() / CLASSTERNUM;
        int count = 0;

        // �����_����ɁAWarningUnit���N���X�^�ɕ�������B
        for (WarningUnit unit : warningUnitList)
        {
            clusters[(int)(count / cutPoint)].add(unit);
            count++;
        }
    }

    /**
     * �N���X�^�̕��ϒl���擾����B
     * @param clusters �N���X�^
     * @return�@���ϒl�̔z��
     */
    private double[] getAverages(final WarningUnitCluster[] clusters)
    {
        double[] averages = new double[CLASSTERNUM];
        for (int num = 0; num < clusters.length; num++)
        {
            averages[num] = clusters[num].average();
        }
        return averages;
    }

    /**
     * �N���X�^��WarningUnit��z�u����B
     * @param clusters
     * @param average
     */
    private void dispatch(final WarningUnit warningUnit, final WarningUnitCluster[] clusters,
            final double[] average)
    {
        int position = 0;
        double minimizeDistant = Double.MAX_VALUE;

        //�@WarningUnit�ƃN���X�^�̏d�S�Ƃ̋������ł��߂��Ȃ�悤�ɁA
        // WarningUnit��z�u����B
        for (int num = 0; num < clusters.length; num++)
        {
            String argsString =
                                warningUnit.getArgs()[PerformanceDoctorFilter.TARGET_VALUE_INDEX].toString();
            double argsNum = Double.parseDouble(argsString);
            double distant = Math.abs(average[num] - argsNum);
            if (distant < minimizeDistant)
            {
                minimizeDistant = distant;
                position = num;
            }
        }
        clusters[position].add(warningUnit);
    }

    /**
     * 2�̃N���X�^���r���A�������ǂ����𒲂ׂ�B<br />
     * �������ǂ����́A�N���X�^�Ɋ܂܂��v�f���Ŕ�r����B
     * @param beforeClusters �Ĕz�u�O�̃N���X�^
     * @param afterClusters �Ĕz�u��̃N���X�^
     * @return true:2�̃N���X�^�������Bfalse:2�̃N���X�^���قȂ�B
     */
    private boolean compare(final WarningUnitCluster[] beforeClusters,
            final WarningUnitCluster[] afterClusters)
    {
        for (int num = 0; num < CLASSTERNUM; num++)
        {
            if (beforeClusters[num].getSize() != afterClusters[num].getSize())
            {
                return false;
            }
        }
        return true;

    }

    /**
     * �N���X�^����WarningUnit�̃��X�g���쐬����B
     * @param clusters �N���X�^�z��B
     * @return �쐬�������X�g
     */
    private List<WarningUnit> clusterToList(final WarningUnitCluster[] clusters)
    {
        List<WarningUnit> list = new ArrayList<WarningUnit>();

        // �e�N���X�^�̍Ō�����擾���A���X�g���쐬����B
        for (WarningUnitCluster cluster : clusters)
        {
            WarningUnit warningUnit = cluster.getLastWarningUnit();
            if (warningUnit != null)
            {
                list.add(warningUnit);
            }
        }
        return list;
    }
}
