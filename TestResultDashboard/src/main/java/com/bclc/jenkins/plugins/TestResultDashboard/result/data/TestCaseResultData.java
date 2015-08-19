package com.bclc.jenkins.plugins.TestResultDashboard.result.data;

import hudson.tasks.junit.CaseResult;

public class TestCaseResultData extends ResultData {

	public TestCaseResultData(CaseResult testResult){
		setName(testResult.getName());
		setPassed(testResult.isPassed());
		setSkipped(testResult.getSkipCount() == testResult.getTotalCount());
		setTotalTests(testResult.getTotalCount());
		setTotalFailed(testResult.getFailCount());
		setTotalPassed(testResult.getPassCount());
		setTotalSkipped(testResult.getSkipCount());
		setTotalTimeTaken(testResult.getDuration());
		evaluateStatus();
		if("FAILED".equalsIgnoreCase(getStatus())){
			setFailureMessage(testResult.getErrorStackTrace());
		}
	}
	
}
