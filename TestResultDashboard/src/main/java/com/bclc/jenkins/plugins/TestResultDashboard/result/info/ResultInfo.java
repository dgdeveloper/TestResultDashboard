package com.bclc.jenkins.plugins.TestResultDashboard.result.info;

import hudson.tasks.junit.PackageResult;
import hudson.tasks.junit.TestResult;

import java.util.Map;
import java.util.TreeMap;

import com.bclc.jenkins.plugins.TestResultDashboard.result.data.BuildResultData;

import net.sf.json.JSONObject;

public class ResultInfo extends Info {
	
	private Map<String,PackageInfo> packageResults = new TreeMap<String, PackageInfo>();
	
	public ResultInfo(TestResult testResult)
	{
		BuildResultData buildResultData = new BuildResultData(testResult);
		this.resultData = buildResultData;
	}
	public void addPackage(PackageResult packageResult){
		String packageName = packageResult.getName();
		PackageInfo packageInfo;
/*		if(packageResults.containsKey(packageName)){
			packageInfo = packageResults.get(packageName);
		} else {
			packageInfo = new PackageInfo();
			packageInfo.setName(packageName);
		}*/
		
		packageInfo = new PackageInfo();
		packageInfo.setName(packageName);
		packageInfo.putPackageResult(packageResult);
		packageResults.put(packageName, packageInfo);		
	}
	
//	public JSONObject getJsonObject(){
//		JSONObject json = new JSONObject();
//		for(String packageName: packageResults.keySet()){
//			json.put(packageName, packageResults.get(packageName).getJsonObject());
//		}
//		return json;
//	}
	
	public Map<String,PackageInfo> getPackageResults(){
		return this.packageResults;
	}
}
