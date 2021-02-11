package logic.util;

import java.util.ArrayList;
import java.util.List;

import logic.view.utils.GUIType;
import logic.view.utils.GraphicControl;

public class Session {

	private String userEmail;
	private String userName;
	private String userSurname;
	private int userPoints;
	private GUIType currView;
	private GraphicControl prevCtrl;
	private GraphicControl currCtrl;
	private List<GUIType> viewQueue;
	
	public Session() {
		this.viewQueue = new ArrayList<>();
	}
	
	public GUIType getPrevView() {
		viewQueue.remove(viewQueue.size()-1);
		currView = viewQueue.get(viewQueue.size()-1);
		return currView;
	}
	
	public GraphicControl getPrevGraphicControl() {
		return this.prevCtrl;
	}
	
	public void setCurrView(GUIType view, GraphicControl ctrl) {
		this.prevCtrl = this.currCtrl;
		this.currView = view;
		this.currCtrl = ctrl; 
		this.viewQueue.add(currView);
	}
	
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public int getUserPoints() {
		return userPoints;
	}

	public void setUserPoints(int userPoints) {
		this.userPoints = userPoints;
	}

	public String getUserSurname() {
		return userSurname;
	}

	public void setUserSurname(String userSurname) {
		this.userSurname = userSurname;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
