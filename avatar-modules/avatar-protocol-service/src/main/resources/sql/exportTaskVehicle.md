queryVinListOfForward
===
select t.vin
  from fwp_vehicle_data t
 where t.task_id = #taskId#
 
queryCreateTime
===
select t.create_at
   from FWP_VEHICLE_DATA t
  where t.task_id = #taskId#
  and t.vin = #vin#
  
deleteByExportTaskId
===
delete from FWP_EXPORT_TASK_VEHICLE where EXPORT_TASK_ID = #exportTaskId#