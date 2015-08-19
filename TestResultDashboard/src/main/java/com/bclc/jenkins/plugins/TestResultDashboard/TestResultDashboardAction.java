package com.bclc.jenkins.plugins.TestResultDashboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.bind.JavaScriptMethod;

import hudson.model.Action;
import hudson.model.Item;
import hudson.model.AbstractProject;
import hudson.model.Actionable;
import hudson.model.Run;
import hudson.tasks.junit.PackageResult;
import hudson.tasks.junit.TestResult;
import hudson.tasks.test.AbstractTestResultAction;
import hudson.util.RunList;

import com.bclc.jenkins.plugins.TestResultDashboard.result.data.ResultData;
import com.bclc.jenkins.plugins.TestResultDashboard.result.info.ClassInfo;
import com.bclc.jenkins.plugins.TestResultDashboard.result.info.PackageInfo;
import com.bclc.jenkins.plugins.TestResultDashboard.result.info.ResultInfo;
import com.bclc.jenkins.plugins.TestResultDashboard.result.info.TestCaseInfo;

public class TestResultDashboardAction extends Actionable implements Action{
	@SuppressWarnings("rawtypes")
	AbstractProject project;
	Map<Integer, ResultInfo> projectBuildResults = new TreeMap<Integer, ResultInfo>();
	
	private List<Integer> buildNumbers = new ArrayList<Integer>() ;
	
	public TestResultDashboardAction(@SuppressWarnings("rawtypes") AbstractProject project){
		this.project = project;
	}
	
	@Override
	public String getSearchUrl() {
		return this.hasPermission() ? Constants.URL : null;
	}

	@Override
	public String getIconFileName() {
        return this.hasPermission() ? Constants.ICONFILENAME : null;
	}

	@Override
	public String getDisplayName() {
        return this.hasPermission() ? Constants.NAME : null;
	}

	@Override
	public String getUrlName() {
        return this.hasPermission() ? Constants.URL : null;
	}


    private boolean hasPermission() {
        return project.hasPermission(Item.READ);
    }
    
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getJsonLoadData() {
		if (isUpdated()) {
			RunList<Run> runs = project.getBuilds();
			Iterator<Run> runIterator = runs.iterator();
			while (runIterator.hasNext()) {
				Run run = runIterator.next();
				int buildNumber = run.getNumber();
				buildNumbers.add(buildNumber);
				List<AbstractTestResultAction> testActions = run.getActions(hudson.tasks.test.AbstractTestResultAction.class);
				for (hudson.tasks.test.AbstractTestResultAction testAction : testActions) {
					TestResult testResult = (TestResult) testAction.getResult();
					ResultInfo resultInfo = new ResultInfo(testResult);
					Collection<PackageResult> packageResults = testResult.getChildren();

					for (PackageResult packageResult : packageResults) { // packageresult	
						resultInfo.addPackage(packageResult);
					}
					projectBuildResults.put(buildNumber, resultInfo);
				}		
			}
		}
	}
	
	public boolean isUpdated(){
		int latestBuildNumber = project.getLastBuild().getNumber();
		return !(buildNumbers.contains(latestBuildNumber));
	}
	
	public List<Integer> getBuilds(){
		return this.buildNumbers;
	}
	
 
    @JavaScriptMethod
    public JSONArray getTestResultStatusByBuildNumber(int buildNumber) {
		JSONArray jsonArray = new JSONArray();
		
    	ResultInfo buildResultInfo = projectBuildResults.get(buildNumber);
    	int totalPassed = 0, totalFailed = 0, totalSkipped = 0, totalTests = 0;
    	Map<String,PackageInfo> packageResultInfo = buildResultInfo.getPackageResults();
    	for (Map.Entry<String, PackageInfo> entry : packageResultInfo.entrySet()) {
    		ResultData packageResultData = entry.getValue().getResultData();
    		totalPassed += packageResultData.getTotalPassed();
    		totalFailed += packageResultData.getTotalFailed();
//    		totalSkipped += packageResultData.getTotalSkipped();
    		totalTests += packageResultData.getTotalTests();
    	}
    	
       JSONObject json1 = new JSONObject();
 	   json1.put("Status","Passed");	
	   json1.put("Value",(totalPassed * 100)/totalTests);
	   json1.put("Count",totalPassed);
	   jsonArray.add(json1);
	   
	   JSONObject json2 = new JSONObject();
 	   json2.put("Status","Failed");	
	   json2.put("Value",(totalFailed * 100)/totalTests);
	   json2.put("Count",totalFailed);
	   jsonArray.add(json2);
	   
//	   JSONObject json3 = new JSONObject();
// 	   json3.put("Status","Skipped");	
//	   json3.put("Value",(totalSkipped * 100)/totalTests);
//	   json3.put("Count",totalFailed);
//	   jsonArray.add(json3);

		return jsonArray;
    }
    
    @JavaScriptMethod
    public JSONArray getFailurePercentageForAreaByBuildNumber(int buildNumber) {
    	JSONArray jsonArray = new JSONArray();
    	
    	ResultInfo buildResultInfo = projectBuildResults.get(buildNumber);
    	Map<String,PackageInfo> packageResultInfo = buildResultInfo.getPackageResults();
    	
    	int totalTests = 0;
    	for (Map.Entry<String, PackageInfo> entry : packageResultInfo.entrySet()) {
    		ResultData packageResultData = entry.getValue().getResultData();
    		totalTests += packageResultData.getTotalTests();
    	}
    	
    	for (Map.Entry<String, PackageInfo> entry : packageResultInfo.entrySet()) {   		
    		ResultData packageResultData = entry.getValue().getResultData();   		
    		String packageName = packageResultData.getName(); 
    		int totalFailed = packageResultData.getTotalFailed();
    		
    		JSONObject json = new JSONObject();
    	 	json.put("PackageName",packageName);	
    		json.put("Value",(totalFailed * 100)/totalTests);
    		jsonArray.add(json);
    	}
    	return jsonArray;
    }
    
    @JavaScriptMethod
    public JSONArray getTestResultTrend() {
    	JSONArray jsonArray = new JSONArray();
    	
    	for (Map.Entry<Integer, ResultInfo> entry : projectBuildResults.entrySet()) {
    		ResultData buildResultData = entry.getValue().getResultData();
    		int totalPassed = buildResultData.getTotalPassed();
    		int totalTests = buildResultData.getTotalTests();
    		
    		JSONObject json = new JSONObject();
    	 	json.put("BuildNumber",entry.getKey());	
    		json.put("Value",(totalPassed * 100)/totalTests);
    		jsonArray.add(json);
    		
    	}
    	
    	
    	return jsonArray;
    }

}
