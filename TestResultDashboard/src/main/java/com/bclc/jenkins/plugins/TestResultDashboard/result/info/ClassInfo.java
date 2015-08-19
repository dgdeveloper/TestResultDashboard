package com.bclc.jenkins.plugins.TestResultDashboard.result.info;

import hudson.tasks.junit.CaseResult;
import hudson.tasks.junit.ClassResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.bclc.jenkins.plugins.TestResultDashboard.result.data.ClassResultData;

import net.sf.json.JSONObject;

public class ClassInfo extends Info{
	
//	private Map<String, TestCaseInfo> tests = new TreeMap<String, TestCaseInfo>();
	private List<TestCaseInfo> tests = new ArrayList<TestCaseInfo>() ;
	
	public void putBuildClassResult(ClassResult classResult){
		ClassResultData classResultData = new ClassResultData(classResult);
//		evaluateStatusses(classResult);
		this.resultData = classResultData;
		addTests(classResult);
//		this.buildResults.put(buildNumber, classResultData);		
	}
	
	private void addTests(ClassResult classResult) {
		for (CaseResult testCaseResult : classResult.getChildren()) {
			String testCaseName = testCaseResult.getName();
			TestCaseInfo testCaseInfo;
/*			if (tests.containsKey(testCaseName)) {
				testCaseInfo = tests.get(testCaseName);
			} else {
				testCaseInfo = new TestCaseInfo();
				testCaseInfo.setName(testCaseName);
			}
			*/
			testCaseInfo = new TestCaseInfo();
			testCaseInfo.setName(testCaseName);

			testCaseInfo.putTestCaseResult(testCaseResult);
//			tests.put(testCaseName, testCaseInfo);
			tests.add(testCaseInfo);
		}
	}
	
//	protected JSONObject getChildrensJson(){
//		JSONObject json = new JSONObject();
//		for(String testName : tests.keySet()){
//			json.put(testName, tests.get(testName).getJsonObject());
//		}
//		return json;
//	}
}
