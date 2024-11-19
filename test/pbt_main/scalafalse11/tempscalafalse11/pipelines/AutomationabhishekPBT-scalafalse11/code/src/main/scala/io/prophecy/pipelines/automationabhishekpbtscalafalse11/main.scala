package io.prophecy.pipelines.automationabhishekpbtscalafalse11

import io.prophecy.libs._
import io.prophecy.pipelines.automationabhishekpbtscalafalse11.config._
import io.prophecy.pipelines.automationabhishekpbtscalafalse11.functions.UDFs._
import io.prophecy.pipelines.automationabhishekpbtscalafalse11.functions.PipelineInitCode._
import io.prophecy.pipelines.automationabhishekpbtscalafalse11.graph._
import org.apache.spark._
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.expressions._
import java.time._

object Main {

  def apply(context: Context): Unit = {
    val df_s3_source_dataset = s3_source_dataset(context)
    create_lookup_table(context, df_s3_source_dataset)
    val df_reformat_customer_data =
      reformat_customer_data(context, df_s3_source_dataset)
    val df_print_execution_success =
      print_execution_success(context, df_reformat_customer_data)
    val df_select_from_temp_view =
      select_from_temp_view(context, df_s3_source_dataset)
  }

  def main(args: Array[String]): Unit = {
    val config = ConfigurationFactoryImpl.getConfig(args)
    val spark: SparkSession = SparkSession
      .builder()
      .appName("AutomationabhishekPBT-scalafalse11")
      .config("spark.default.parallelism",             "4")
      .config("spark.sql.legacy.allowUntypedScalaUDF", "true")
      .enableHiveSupport()
      .getOrCreate()
    val context = Context(spark, config)
    spark.conf.set("prophecy.metadata.pipeline.uri",
                   "pipelines/AutomationabhishekPBT-scalafalse11"
    )
    registerUDFs(spark)
    MetricsCollector.instrument(spark,
                                "pipelines/AutomationabhishekPBT-scalafalse11"
    ) {
      apply(context)
    }
  }

}
