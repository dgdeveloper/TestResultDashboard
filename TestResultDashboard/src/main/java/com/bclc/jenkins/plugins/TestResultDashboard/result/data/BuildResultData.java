package com.bclc.jenkins.plugins.TestResultDashboard.result.data;

import hudson.tasks.junit.TestResult;


public class BuildResultData extends ResultData {

	public BuildResultData(TestResult testResult) {

		setName(testResult.getName());
		setPassed(testResult.getFailCount()==0);
		setSkipped(testResult.getSkipCount() == testResult.getTotalCount());
		setTotalTests(testResult.getTotalCount());
		setTotalFailed(testResult.getFailCount());
		setTotalPassed(testResult.getPassCount());
		setTotalSkipped(testResult.getSkipCount());
		setTotalTimeTaken(testResult.getDuration());
		evaluateStatus();
	}
}
