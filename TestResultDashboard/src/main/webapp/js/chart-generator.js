 var statusColors = {
    "passed" :"#92D050",
    "failed" :"#F37A7A",
    "skipped" :"#FDED72",
    "total" :"#67A4F5",
    "na" :""
};

function generateChart(buildNumber) {
        generateLineChart_TestResultTrend();
        generatePieChart_TestResultByStatus(buildNumber);
        generateBarChart_FailuresByAreas(buildNumber);

}

function generateLineChart_TestResultTrend() {

       	var chartData
        var highchartsData = []; // this is data for highcharts  
        var chartCategories = [];
	   								
	    remoteAction.getTestResultTrend($j.proxy(
             function(t){
                chartData = t.responseObject();
                $j.each(chartData, function(i, e) {
                     var buildNumber = e.BuildNumber
	            	 chartCategories.push(buildNumber);
	            	 var PassedPercentage = e.Value
	            	 highchartsData.push(PassedPercentage);
                
                });

                $j(function () {$j("#linechart").highcharts(getLineChartConfig(chartCategories, highchartsData))});

             },this)
            )
  	
}


function getLineChartConfig(chartCategories, highchartsData){
    var linechart = {
        title: {
            text: 'Test Result Trend %',
            x: -20 //center
        },
        xAxis: {
            title: {
                text: 'Build number'
            },
            categories: chartCategories
        },
        yAxis: {
            title: {
                text: 'Percentage (%)'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        credits: {
            enabled: false
        },
        tooltip: {
            headerFormat: '<b>Build no: {point.x}</b><br>',
            shared: true,
            crosshairs: true
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 0
        },
        colors : [statusColors["passed"], statusColors["failed"], statusColors["skipped"],statusColors["total"]]
        ,
        /*plotOptions: {
            series: {
                cursor: 'pointer',
                point: {
                    events: {
                        click: function (e) {
                            var x1 = this.x;
                            var category = this.category;
                            var passed = this.series.chart.series[0].data[x1].y;
                            var failed = this.series.chart.series[1].data[x1].y;
                            var skipped = this.series.chart.series[2].data[x1].y;
                            var total = this.series.chart.series[3].data[x1].y;
                            var resultTitle = 'Build details for build: '+category;
                            generatePieChart(calculatePercentage(passed,failed,skipped,total),resultTitle);

                        }
                    }
                }
            }
        },*/
        series: [{
            name: 'Failure %',
            data: highchartsData
        }]
    }

    return linechart;
}
    
function generatePieChart_TestResultByStatus(buildNumber) {
            var chartData
            var highchartsData = []; // this is data for highcharts
    
        	remoteAction.getTestResultStatusByBuildNumber(buildNumber,$j.proxy(
             function(t){
                chartData = t.responseObject();
                $j.each(chartData, function(i, e) {
                	highchartsData.push({
						    name:   e.Status,
						    y: e.Value,
						    count: e.Count
						});
						                });

                $j(function () {$j("#piechart").highcharts(getPieChartConfig(highchartsData,"Test Result By Status %"))});

             },this)
            )
            
          
    }
    
function getPieChartConfig(highchartsData, resultTitle){
    var pieChart = {
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: 1,//null,
            plotShadow: false,
            type: 'pie',
        },
        credits: {
            enabled: false
        },
        title: {
            text: resultTitle ? resultTitle : 'Test Result By Status %'
        },
        tooltip: {
            pointFormat: '<b>{point.count}</b> ({point.percentage:.1f}%)'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                    style: {
                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                    }
                }
            }
        },
        colors : [statusColors["passed"], statusColors["failed"], statusColors["skipped"],statusColors["total"]],
        series: [{
            data: highchartsData
        }]
    }
    return pieChart;
}


    function generateBarChart_FailuresByAreas(buildNumber) {
    	var chartData
        var highchartsData = []; // this is data for highcharts  
        var chartCategories = [];
	   								
	    remoteAction.getFailurePercentageForAreaByBuildNumber(buildNumber,$j.proxy(
             function(t){
                chartData = t.responseObject();
                $j.each(chartData, function(i, e) {
                     var areaName = e.PackageName
	            	 chartCategories.push(areaName);
	            	 var failurePercentage = e.Value
	            	 highchartsData.push(failurePercentage);
                
                });

                $j(function () {$j("#barchart").highcharts(getBarChartConfig(chartCategories, highchartsData))});

             },this)
            )
            

    }
    
    function getBarChartConfig(chartCategories, highchartsData){
    var barchart = {
        chart: {
            type: 'bar'
        },
        title: {
            text: 'Failures by Area',
            x: -20 //center
        },
        xAxis: {
            title: {
                text: 'Area'
            },
            categories: chartCategories
        },
        yAxis: {
            title: {
                text: 'Failure %'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        colors : [statusColors["failed"]],
        credits: {
            enabled: false
        },
        tooltip: {
            headerFormat: '<b>Area:</b> {point.x}',
            shared: true,
            crosshairs: true
        },
        plotOptions: {
            bar: {
                dataLabels: {
                    enabled: true
                }
            }
        },
        legend: {
            reversed: true
        },
        series: [{
            name: 'Failure %',
            data: highchartsData
        }]
    }

    return barchart;
}






