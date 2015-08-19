package com.bclc.jenkins.plugins.TestResultDashboard.result.info;

import com.bclc.jenkins.plugins.TestResultDashboard.result.data.TestCaseResultData;

import hudson.tasks.junit.CaseResult;

import net.sf.json.JSONObject;

public class TestCaseInfo extends Info {


	
	public void putTestCaseResult(CaseResult testCaseResult) {
		TestCaseResultData testCaseResultData = new TestCaseResultData(testCaseResult);
//		evaluateStatusses(testCaseResult);
		this.resultData = testCaseResultData;
//		this.buildResults.put(buildNumber, testCaseResultData);
	}
	

//	@Override
//	protected JSONObject getChildrensJson() {
//
//		return new JSONObject();
//	}

}
