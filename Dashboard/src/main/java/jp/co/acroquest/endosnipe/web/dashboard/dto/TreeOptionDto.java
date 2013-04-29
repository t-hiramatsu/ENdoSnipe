package jp.co.acroquest.endosnipe.web.dashboard.dto;

/**
 * ツリーオプションのDTOクラス。
 * 
 * @author miyasaka
 *
 */
public class TreeOptionDto {
	/** ツリーID。 */
	private String id;
	
	/** ツリーの表示名。 */
	private String data;
	
	/** 親ノードのID。 */
	private String parentTreeId;
	
	/** アイコンの種類。 */
	private String icon;
	
	/**
	 * ツリーIDを取得する。
	 * 
	 * @return ツリーID
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * ツリーIDを設定する。
	 * 
	 * @param id ツリーID
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * ツリーの表示名を取得する。
	 * 
	 * @return ツリーの表示名
	 */
	public String getData() {
		return data;
	}
	
	/**
	 * ツリーの表示名を設定する。
	 * 
	 * @param data ツリーの表示名
	 */
	public void setData(String data) {
		this.data = data;
	}
	
	/**
	 * 親ノードのIDを取得する。
	 * 
	 * @return 親ノードのID
	 */
	public String getParentTreeId() {
		return parentTreeId;
	}
	
	/**
	 * 親ノードのIDを設定する。
	 * 
	 * @param parentTreeId 親ノードのID
	 */
	public void setParentTreeId(String parentTreeId) {
		this.parentTreeId = parentTreeId;
	}
	
	/**
	 * アイコンの種類を取得する。
	 * 
	 * @return アイコンの種類
	 */
	public String getIcon() {
		return icon;
	}
	
	/**
	 * アイコンの種類を設定する。
	 * 
	 * @param icon アイコンの種類
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
}
