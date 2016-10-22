package com.example.pages;

import com.contact.service.ContactServiceProxy;
import com.contact.service.IContactService;
import com.contact.service.IContactService.RY_ATTRIBUTEYNAME;
import com.contact.service.IContactService.SB_ATTRIBUTEYNAME;
import com.example.invoke.SystemPropInvoke;
import com.example.yemian.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;



/**
 * Created by yangmingyue on 16/8/5.
 */
public class ZzwPage extends TabActivity {

    private TabHost tabHost;
    private Intent mainConfig;
    private Intent minorConfig;
    private String strValue;
    IContactService contactservice = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_zzw_page);
        tabHost = getTabHost();
        initIntent();
        addSpec();
    }

    private void initIntent() {
        mainConfig = new Intent(this, ZzwMainPage.class);
        minorConfig = new Intent(this, ZzwMinorPage.class);
    }

    private void addSpec() {
       
        TabHost.TabSpec mainSpec = tabHost.newTabSpec("main").setIndicator("主配置").setContent(mainConfig);
        tabHost.addTab(mainSpec);
        strValue=SystemPropInvoke.SysPropGet("ro.product.name", null);
        contactservice = ContactServiceProxy.getInstance();

		int vmf = contactservice.getSelfVmf();

		String sbbh = contactservice.getNodeAttributeById(vmf,
				RY_ATTRIBUTEYNAME.NODEATTRI_SBBH);
		String CTJD1=contactservice.getNodeAttributeById(sbbh,
				SB_ATTRIBUTEYNAME.NODEATTRI_CTJD1);
		String CTJD2=contactservice.getNodeAttributeById(sbbh,
				SB_ATTRIBUTEYNAME.NODEATTRI_CTJD2);
        if (strValue.equals("full_lc1860beta")){
        	if(CTJD1.length()!=0&&CTJD2.length()!=0){
        		 TabHost.TabSpec minorSpec = tabHost.newTabSpec("minor").setIndicator("从配置").setContent(minorConfig);
                 tabHost.addTab(minorSpec); 
        	}
        }
       
    }

}
