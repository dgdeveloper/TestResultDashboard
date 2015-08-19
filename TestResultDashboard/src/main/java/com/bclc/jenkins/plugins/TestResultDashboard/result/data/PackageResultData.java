package com.bclc.jenkins.plugins.TestResultDashboard.result.data;


import hudson.tasks.junit.PackageResult;

public class PackageResultData extends ResultData{
	
	
	public PackageResultData(PackageResult packageResult){
		setName(packageResult.getName());
		setPassed(packageResult.getFailCount()==0);
		setSkipped(packageResult.getSkipCount() == packageResult.getTotalCount());
		setTotalTests(packageResult.getTotalCount());
		setTotalFailed(packageResult.getFailCount());
		setTotalPassed(packageResult.getPassCount());
		setTotalSkipped(packageResult.getSkipCount());
		setTotalTimeTaken(packageResult.getDuration());
		evaluateStatus();
	}

}
