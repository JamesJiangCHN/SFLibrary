package com.successfactors.library.client.widget;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Label;
import com.successfactors.library.client.SFLibrary;

public class NavigationPaneHeader extends HLayout {

  private static final String NAVIGATION_PANE_HEADER_HEIGHT = "27px";
  private static final String NAVIGATION_PANE_HEADER_LABEL_DISPLAY_NAME = "Workplace";
  private static final String CONTEXT_AREA_HEADER_LABEL_DISPLAY_NAME = "Activities";
  
  private Label navigationPaneHeaderLabel; 
  private Label contextAreaHeaderLabel; 
		  
  public NavigationPaneHeader() {
	super();
		
	GWT.log("初始化：NavigationPaneHeader", null);
	
    // Initialize the Navigation Pane Header layout container
	this.setStyleName("crm-NavigationPane-Header");	
	this.setHeight(NAVIGATION_PANE_HEADER_HEIGHT);		 
		
	// Initialize the Navigation Pane Header Label
    navigationPaneHeaderLabel = new Label(); 
    navigationPaneHeaderLabel.setStyleName("crm-NavigationPane-Header-Label");
    navigationPaneHeaderLabel.setWidth(SFLibrary.WEST_WIDTH);
    navigationPaneHeaderLabel.setContents(NAVIGATION_PANE_HEADER_LABEL_DISPLAY_NAME); 
    navigationPaneHeaderLabel.setAlign(Alignment.LEFT);  
    navigationPaneHeaderLabel.setOverflow(Overflow.HIDDEN); 
	    
	// Initialize the Context Area Header Label
    contextAreaHeaderLabel = new Label(); 
    contextAreaHeaderLabel.setStyleName("crm-ContextArea-Header-Label");
    contextAreaHeaderLabel.setContents(CONTEXT_AREA_HEADER_LABEL_DISPLAY_NAME); 
    contextAreaHeaderLabel.setAlign(Alignment.LEFT);  
    contextAreaHeaderLabel.setOverflow(Overflow.HIDDEN); 
	    			  
    // Add the Labels to the Navigation Pane Header layout container	    			  
	this.addMember(navigationPaneHeaderLabel);	
	this.addMember(contextAreaHeaderLabel);		 
  }
	  
  public Label getNavigationPaneHeaderLabel() {
    return navigationPaneHeaderLabel;
  }
	  
  public Label getContextAreaHeaderLabel() {
    return contextAreaHeaderLabel;
  } 
	  
  public void setNavigationPaneHeaderLabelContents(String contents) {
    navigationPaneHeaderLabel.setContents(contents);
  } 
	  
  public String getNavigationPaneHeaderLabelContents() {
	return navigationPaneHeaderLabel.getContents();
  } 
	  
  public void setContextAreaHeaderLabelContents(String contents) {
	contextAreaHeaderLabel.setContents(contents);
  }   
	  
  public String getContextAreaHeaderLabelContents() {
	return contextAreaHeaderLabel.getContents();
  }    
}