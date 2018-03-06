<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="modulename" uri="http://mysite.com.ph/taglib"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="customTag" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<h1 class="blue-header">
	<spring:message code="ph.com.mysite.tbg.modulename.message.title.listOfReports" />
</h1>
<br />
<br />

<table id="reportsDataTable" class="branch-report-table">
	<thead>
		<tr>
			<th></th>
			<th class="mdl-data-table__cell--non-numeric ">
				<spring:message code="ph.com.mysite.tbg.modulename.message.TellerReport.label.reportName" />
			</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td></td>
			<td></td>
		</tr>
	</tbody>
</table>

<script type="text/javascript">

	var contextPath = "${pageContext.request.contextPath}";
	
	var showCalender = function() {
		var now = new Date();
		$("#reportsDataTable tr td input.Date").datepicker({
			maxDate: '0',
	        yearRange: '-100:+0',
			changeMonth: true,
			changeYear: true,
			dateFormat:"mm/dd/yy"
		});
		$("#reportsDataTable tr td input.Date").datepicker("setDate", new Date());
	};

	function createRenderedButtons(data) {
		
		var renderedButtons = 
			"<table cellspacing='0' cellpadding='0'>"
			+"<tbody align='right'>"
			+ "<tr>"
			+ "<td>"
			+ "Transaction Dates: "
			+ "</td>"
			+ "<td rowspan='3'>"
			+ "<button class='push-btn' style='text-transform:uppercase;'>Print</button>" 
			+ "</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td>"
			+ "From: <input name='startDate' id='startDate"+data.id+"' class='Date' type='text'/>"
			+ " To: <input name='endDate' id='endDate"+data.id+"' class='Date' type='text'/>"
			+ "</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td>";
			
		renderedButtons = renderedButtons 
		    + "<span id='errorMsg' style='color:red; font-weight: normal;'></span>";
			
		renderedButtons = renderedButtons 
			+ "</td>"
			+ "</tr>"
			+ "</tbody>"
			+ "</table>";
			
		return renderedButtons;
	}

	var table;
	function generateDataTable() {
		var reportUrl = contextPath + "/branch-report/getAllReports";

		table = $('#reportsDataTable').DataTable({
			"order" : [ [ 1, 'asc' ], [ 0, 'asc' ] ],
			"destroy" : true,
			"columns" : [ {
				"data" : "id",
				"visible" : false,
				"sortable" : false
			}, {
				"data" : "name",
				"name" : "name",
				"sortable" : false

			}, {
				"data" : null,
				"sortable" : false,
				"className" : "dt-right"
				
			} ],
			"sort" : false,
			"filter" : false,
			"pagingType" : "full_numbers",
			"lengthChange" : false,
			"processing" : true,
			"serverSide" : true,
			"pageLength" : "${maxRecords}",
			"ajax" : {
				"url" : reportUrl,
				"data" : function(data) {
					recreate(data);
				}
			},
			"columnDefs" : [ {
				"targets" : -1,
				"data" : null,
				"render" : function(data, type, full, meta) {
					return createRenderedButtons(data);
				}
			} ]
		});
		
		setTimeout('showCalender()', 1000);
	}

	function recreate(data) {
		for (var i = 0; i < data.order.length; i++) {
			order = data.order[i];
			order.columnName = data.columns[order.column].data;
		}
		for (var i = 0; i < data.columns.length; i++) {
			column = data.columns[i];
			column.searchRegex = column.search.regex;
			column.searchValue = column.search.value;
			delete (column.search);
		}
	}

	// link to each transaction detail
	$('#reportsDataTable tbody')
			.on(
					'click',
					'button',
					function() {

						var data = table.row($(this).parents('tr')).data();
						var startDate = $(this).closest("tbody")
								.find("#startDate"+data.id).val();
						var endDate = $(this).closest("tbody").find("#endDate"+data.id)
								.val();
						var startDateElement = $(this).closest("tbody").find("#startDate"+data.id);
						var endDateElement = $(this).closest("tbody").find("#endDate"+data.id);
								
						var bothDatesAreValid = Utilities.isValidDate(startDateElement.val()) &&
									Utilities.isValidDate(endDateElement.val());
						var searchDatesAreNotEmpty = startDateElement.val() != '' && endDateElement.val() != '';
						var bothDatesAreCorrect = moment(startDateElement.val(), "MM/DD/YYYY", true).isValid() &&
							  moment(endDateElement.val(), "MM/DD/YYYY", true).isValid();
						
						var errorMsg;
						var isValid = true;
						
						if( !bothDatesAreCorrect || !bothDatesAreValid || !searchDatesAreNotEmpty ){
							//errorMsg = "Invalid Date Range. Don't leave either as blank.";
							errorMsg = "Invalid Date Range.";
							isValid = false;
						} else {
							
							var patt = /^\d{2}\/\d{2}\/\d{4}$/;
				
							if(!patt.test(startDate) || !patt.test(endDate)){
								
								//errorMsg = "Invalid Date Range. Invalid Date Format.";
								errorMsg = "Invalid Date Range.";
								isValid=false;
																
							} else {
							
								var d1 = Date.parse(startDate);
								var d2 = Date.parse(endDate);
								var currentDate = new Date();
								
								if(d1 > d2){
									//errorMsg = "Invalid Date Range. Start Date must not be greater than End Date.";
									errorMsg = "Invalid Date Range.";
									isValid = false;
								} 
								
								if(d1 > currentDate || d2 > currentDate){
									errorMsg = "Invalid Date Range."; //can't be future date
									isValid = false;
								}
							
							}
						}
						
						if (!isValid) {
						    $(this).closest("tbody").find("#errorMsg").html(errorMsg);
						} else {
							$(this).closest("tbody").find("#errorMsg").html("");
						
							var encodedStartDate = startDate.split("/").join("%2F");
							var encodedEndDate = endDate.split("/").join("%2F");
	
							window.open(contextPath
									+ "/branch-report/viewer?file="
									+ contextPath + "/branch-report/download/"
									+ data.id + "%3FstartDate%3D" + encodedStartDate
									+ "%26endDate%3D" + encodedEndDate, "_blank");
						}
					});

	$("#generateTable").click(function() {
		generateDataTable();
	});

	generateDataTable();
</script>