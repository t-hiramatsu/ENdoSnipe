/*
 * Copyright (c) 2004-2009 SMG Co., Ltd. All Rights Reserved.
 * Please read the associated COPYRIGHTS file for more details.
 *
 * THE  SOFTWARE IS  PROVIDED BY  SMG Co., Ltd., WITHOUT  WARRANTY  OF
 * ANY KIND,  EXPRESS  OR IMPLIED,  INCLUDING BUT  NOT LIMITED  TO THE
 * WARRANTIES OF  MERCHANTABILITY,  FITNESS FOR A  PARTICULAR  PURPOSE
 * AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package jp.co.acroquest.endosnipe.report.converter.util.calc;

/**
 * Object�������f�[�^�^�ɓK�������v�Z���������s���邽�߂̃C���^�t�F�[�X
 * 
 * @author M.Yoshida
 */
public interface Calculator 
{
	/**
	 * 2�̃f�[�^���u���Z�v�������ʂ�Ԃ��B
	 * 
	 * @param obj1 �f�[�^�P
	 * @param obj2 �f�[�^�Q
	 * @return ���Z����
	 */
	public Object add(Object obj1, Object obj2);

	/**
	 * 2�̃f�[�^���u���Z�v�������ʂ�Ԃ��B
	 * 
	 * @param obj1 �f�[�^�P
	 * @param obj2 �f�[�^�Q
	 * @return ���Z����
	 */
	public Object sub(Object obj1, Object obj2);

	/**
	 * 2�̃f�[�^���u��Z�v�������ʂ�Ԃ��B
	 * 
	 * @param obj1 �f�[�^�P
	 * @param obj2 �f�[�^�Q
	 * @return ��Z����
	 */
	public Object mul(Object obj1, Object obj2);

	/**
	 * 2�̃f�[�^���u���Z�v�������ʂ�Ԃ��B
	 * 
	 * @param obj1 �f�[�^�P
	 * @param obj2 �f�[�^�Q
	 * @return ���Z����
	 */
	public Object div(Object obj1, Object obj2);
	
	/**
	 * ������\���Ŏ����ꂽ�l��K������^�̃I�u�W�F�N�g�ɕϊ����ĕԂ��B
	 * 
	 * @param str �l�̕�����\��
	 * @return �l
	 */
	public Object immediate(String str);
}
