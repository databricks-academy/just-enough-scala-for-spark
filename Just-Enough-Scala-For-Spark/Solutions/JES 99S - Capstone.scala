// Databricks notebook source
// MAGIC %md-sandbox
// MAGIC 
// MAGIC <div style="text-align: center; line-height: 0; padding-top: 9px;">
// MAGIC   <img src="https://databricks.com/wp-content/uploads/2018/03/db-academy-rgb-1200px.png" alt="Databricks Learning" style="width: 600px">
// MAGIC </div>

// COMMAND ----------

// MAGIC %md
// MAGIC ##![Spark Logo Tiny](https://files.training.databricks.com/images/105/logo_spark_tiny.png) Revenue Insights of a Sports Retailer

// COMMAND ----------

// MAGIC %md
// MAGIC 
// MAGIC This is a **Capstone** project, which is design to validate your learnings from the **Just Enough Scala for Spark** Course. 
// MAGIC 
// MAGIC This Capstone presents you with an opportunity, where you can apply all your learnings including implmentation of classes, methods, exception handling, collections and functional programming to a large problem.

// COMMAND ----------

// MAGIC %md
// MAGIC 
// MAGIC ### ![Spark Logo Tiny](https://files.training.databricks.com/images/105/logo_spark_tiny.png) Problem Description
// MAGIC 
// MAGIC A sports retailer computes revenue performance of a company every end of the year.
// MAGIC 
// MAGIC The revenue related information is available in comma separated value format. All quarterly revenue information is available as a string, where each substring separated by a **;** contains information about a specific product category in a quarter (in a financial year). 
// MAGIC 
// MAGIC Each substring contains following information as  **comma** separated values. 
// MAGIC - which quarter of the year (Q1/Q2/Q3/Q4 etc.)
// MAGIC - name of the product category
// MAGIC - total revenue generated in the financial quarter.
// MAGIC 
// MAGIC 
// MAGIC **A sample data string is shown below.**
// MAGIC 
// MAGIC ```
// MAGIC 
// MAGIC  "Q1-2018   Exercise_Fitness    10.33, 
// MAGIC  Q1-2018   Outdoor_Play_Equipment  7.85, 
// MAGIC  Q1-2018   Winter_Sports   3.45"
// MAGIC  
// MAGIC ```
// MAGIC 
// MAGIC Where each substring separated by **;** is considered a **record**. In the subsequent section, all references to **record** means this substring (e.g. *Q1-2018,Exercise_Fitness,10.33*).

// COMMAND ----------

// MAGIC %md
// MAGIC 
// MAGIC This capstone asks the participants to develop the module in 8 steps.
// MAGIC 
// MAGIC 1. **Step 1:** Initialize the string 
// MAGIC 2. **Step 2:** Parse and Validate a record (substring) to figure out if the substring is properly formatted or not.
// MAGIC 3. **Step 3:** Define a method to filter good and bad records from the complete string.
// MAGIC 4. **Step 4:** Define a case class to represent a good record and convert all good records to an array of case classes
// MAGIC 5. **Step 5:** Create a Class that encapsulates all good records and provides methods to calculate insights
// MAGIC     - the total revenue generated by the sports company
// MAGIC     - which takes a product category and returns the total revenue generated

// COMMAND ----------

// MAGIC %md
// MAGIC 
// MAGIC ### ![Spark Logo Tiny](https://files.training.databricks.com/images/105/logo_spark_tiny.png) Step 1: Initialize the String
// MAGIC 
// MAGIC The string that contains the data is given below. Store the string in a variable named **RevenueInfo**.
// MAGIC 
// MAGIC ```
// MAGIC  "Q1-2018,Exercise_Fitness,10.33; 
// MAGIC   Q1-2018,Outdoor_Play_Equipment,7.85; 
// MAGIC   Q1-2018,Winter_Sports,3.45;
// MAGIC   Q2-2018,Exercise_Fitness,7.63; 
// MAGIC   Q2-2018,Outdoor_Play_Equipment,5.05; 
// MAGIC   Q2-2018,Winter_Sports,-;
// MAGIC   Q3-2018,Exercise_Fitness,1.31; 
// MAGIC   Q3-2018,Outdoor_Play_Equipment,3.95; 
// MAGIC   Q3-2018,Winter_Sports,1.50;
// MAGIC   Q4-2018,Exercise_Fitness,5.71; 
// MAGIC   Q4-2018,Outdoor_Play_Equipment,6.52; 
// MAGIC   Q4-2018,Winter_Sports,4.15"
// MAGIC ```                     

// COMMAND ----------

//ANSWERS

val RevenueInfo = """Q1-2018,Exercise_Fitness,10.33; 
                     Q1-2018,Outdoor_Play_Equipment,7.85; 
                     Q1-2018,Winter_Sports,3.45;
                     Q2-2018,Exercise_Fitness,7.63; 
                     Q2-2018,Outdoor_Play_Equipment,5.05; 
                     Q2-2018,Winter_Sports,-;
                     Q3-2018,Exercise_Fitness,1.31; 
                     Q3-2018,Outdoor_Play_Equipment,3.95; 
                     Q3-2018,Winter_Sports,1.50;
                     Q4-2018,Exercise_Fitness,5.71; 
                     Q4-2018,Outdoor_Play_Equipment,6.52; 
                     Q4-2018,Winter_Sports,4.15"""

// COMMAND ----------

// MAGIC %md
// MAGIC 
// MAGIC ### ![Spark Logo Tiny](https://files.training.databricks.com/images/105/logo_spark_tiny.png) Step 2: Parse and Validate a record
// MAGIC 
// MAGIC In this step, validate a substring that represents a record e.g. **"Q1-2018,Exercise_Fitness,10.33"**
// MAGIC 
// MAGIC 1. Define a method **validateRecord**, which takes a string named **recStr** (representing a record)
// MAGIC 2. Parse the string by **comma**
// MAGIC 3. Validate that the string contains 3 fields 
// MAGIC    - If there are less than 3 fields then throw an exception
// MAGIC 4. Validate if the thrid field is numeric or not (Note: try type casting to float) 
// MAGIC    - If not numeric then throw an exception
// MAGIC 5. Surround the complete implementation by **`try`**/**`catch`** to handle exceptions
// MAGIC 6. If the record is valid (i.e. passes both the checks), 
// MAGIC    - then return a tuple with tag (GOOD) and the record string as ("GOOD", recStr)
// MAGIC    - if the record is not valid then return a tuple with tag (BAD) and the record string as ("BAD", recStr)
// MAGIC    
// MAGIC    Note: **recStr** is the original string passed to the method

// COMMAND ----------

//ANSWERS

def validateRecord(recStr: String) = {
  
  // Split the string by comma
  val fields = recStr.split(",")

  // Wrap the code around try/catch to handle exceptions
  try {
  
    // If the number of fields in the string is less than 3, then throw an exception
    if(fields.size < 3){
      throw new Exception("Expected 3 fields. Found only " + fields.size)
    }
      
    // Convert the third field (revenue) into float type
    val revenue = fields(2).toFloat
    
    // If we have reached here without any problem, then it is a good rec. Tag the record GOOD and return
    ("GOOD", recStr)
    
  } catch {
    
    // If we have reached here then it is a bad rec. Tag the record BAD and return
    case e: Exception => ("BAD", recStr)
    
  }
}

// COMMAND ----------

// TEST - Run this cell to test your solution.

var test1Str = validateRecord("Q1-2018,Exercise_Fitness,10.13")
var test1StrExpected = ("GOOD","Q1-2018,Exercise_Fitness,10.13")

var test2Str = validateRecord("Q1-2018,Exercise_Fitness,as")
var test2StrExpected = ("BAD","Q1-2018,Exercise_Fitness,as")

var test3Str = validateRecord("Q1-2018,Exercise_Fitness")
var test3StrExpected = ("BAD","Q1-2018,Exercise_Fitness")

// rounding off the results to two decimal places for comparison
assert (test1Str == test1StrExpected, s"Expected the result to be ${test1StrExpected} but found ${test1Str}")
assert (test2Str == test2StrExpected, s"Expected the result to be ${test2StrExpected} but found ${test2Str}")
assert (test3Str == test3StrExpected, s"Expected the result to be ${test3StrExpected} but found ${test3Str}")

// COMMAND ----------

// MAGIC %md
// MAGIC 
// MAGIC ### ![Spark Logo Tiny](https://files.training.databricks.com/images/105/logo_spark_tiny.png) Step 3(a): Define a method to filter good and bad records
// MAGIC 
// MAGIC In this step parse the string **RevenueInfo** into substrings and use the method **validateRecord** to validate each substring.
// MAGIC 
// MAGIC 1. Define a method called **parsetData**, which takes the complete string stored in **RevenueInfo**
// MAGIC 2. Parse the string by a **semicolon(;)**  character, which returns an **Array** of substrings
// MAGIC 3. Iterate through all the substrings (using functional programming features), validate each substring
// MAGIC 4. Filter all good records that contains the tag **GOOD**
// MAGIC 5. Filter all bad records that contains the tag **BAD**
// MAGIC 6. Return a tuple that contains good and bad records as  **(array of good records, array of bad records)**

// COMMAND ----------

//ANSWERS

import scala.collection.immutable.Vector

def parsetData(dataStr: String) = {
  
  // split the string by ';' to get individual record strings
  val recList = dataStr.split(";")
  
  // validate each record
  val allRecs = recList.map(rec => validateRecord(rec.trim) )
  
  // based on GOOD and BAD tag, store in goodRecs and badRecs variable
  val goodRecs = allRecs.filter(rec => rec._1 == "GOOD")
  val badRecs = allRecs.filter(rec => rec._1 == "BAD")

  // return the tuple
  (goodRecs, badRecs)
  
}

// COMMAND ----------

// MAGIC %md
// MAGIC 
// MAGIC ### ![Spark Logo Tiny](https://files.training.databricks.com/images/105/logo_spark_tiny.png) Step 3(b): Filter good and bad records
// MAGIC 
// MAGIC 1. Invoke the above method **parseData** with **RevenueInfo** as a parameter.
// MAGIC 2. Store the returned tuple values (containing arrays of good and bad records) into two variables called **goodrecs** and **badrecs**.

// COMMAND ----------

//ANSWERS

val (goodrecs, badrecs) = parsetData(RevenueInfo)

// COMMAND ----------

//TEST - Run this cell to test your solution.

var goodrecsLen = goodrecs.size
var goodrecsLenExpected = 11

var badrecsLen = badrecs.size
var badrecsLenExpected = 1

// rounding off the results to two decimal places for comparison
assert (goodrecsLen == goodrecsLenExpected, s"Expected the result to be ${goodrecsLenExpected} but found ${goodrecsLen}")
assert (badrecsLen == badrecsLenExpected, s"Expected the result to be ${badrecsLenExpected} but found ${badrecsLen}")

// COMMAND ----------

// MAGIC %md
// MAGIC ### ![Spark Logo Tiny](https://files.training.databricks.com/images/105/logo_spark_tiny.png) Step 4 (a): Define Case Class to represent each good record
// MAGIC 
// MAGIC 1. Define a case class named **CategoryQuarterlyRecord** with three fields 
// MAGIC   - **quarter** as a **String**
// MAGIC   - **category** as a **String**
// MAGIC   - **revenue** as a **Float**

// COMMAND ----------

//ANSWERS

case class CategoryQuarterlyRecord(quarter:String, category:String, revenue:Float)

// COMMAND ----------

// MAGIC %md
// MAGIC ### ![Spark Logo Tiny](https://files.training.databricks.com/images/105/logo_spark_tiny.png) Step 4(b): Define a method to convert a substring into case class
// MAGIC 
// MAGIC 1. Define a method **convertToClass**, which
// MAGIC   - Takes the **string** representing a good record ( e.g. *"Q1-2018,Exercise_Fitness,10.33"*) 
// MAGIC   - Parses the string by **comma** 
// MAGIC   - Returns a case class of type **CategoryQuarterlyRecord**

// COMMAND ----------

//ANSWERS

// Define the method convertToClass()

def convertToClass(goodrec: String): CategoryQuarterlyRecord = {
  
  // split the record by ','
  val fields = goodrec.split(",")

  // create and return the case class
  CategoryQuarterlyRecord(fields(0), fields(1), fields(2).toFloat)
  
}

// COMMAND ----------

//TEST - Run this cell to test your solution.

var caseClass = convertToClass("Q1-2018,Exercise_Fitness,10.33")
var caseClassExpected = CategoryQuarterlyRecord("Q1-2018","Exercise_Fitness","10.33".toFloat)

// rounding off the results to two decimal places for comparison
assert (caseClass == caseClassExpected, s"Expected the result to be ${caseClassExpected} but found ${caseClass}")

// COMMAND ----------

// MAGIC %md
// MAGIC ### ![Spark Logo Tiny](https://files.training.databricks.com/images/105/logo_spark_tiny.png) Step 4(c): Convert all good records to case classes
// MAGIC 
// MAGIC 1. We have all the good record string stored in **goodrecs** variables.
// MAGIC 2. Convert all the string to an Array of **CategoryQuarterlyRecord** classes and store in a variable **categoryRecs**.

// COMMAND ----------

//ANSWERS

val categoryRecs = goodrecs.map(rec => convertToClass(rec._2))

// COMMAND ----------

//TEST - Run this cell to test your solution.

var categoryRecsLen = goodrecs.size
var categoryRecsLenExpected = 11

// rounding off the results to two decimal places for comparison
assert (categoryRecsLen == categoryRecsLenExpected, s"Expected the result to be ${categoryRecsLenExpected} but found ${categoryRecsLen}")

// COMMAND ----------

// MAGIC %md
// MAGIC 
// MAGIC ### ![Spark Logo Tiny](https://files.training.databricks.com/images/105/logo_spark_tiny.png) Step 5(a): Create a Class that encapsulates all good records and provides insights
// MAGIC 
// MAGIC 1. Create a class named **CompanyPerformance**
// MAGIC 2. The class constructor takes an argument of *Array of CategoryQuarterlyRecord* and stores in a class level variable.
// MAGIC 3. Implement a method **getTotalRevenue**, which returns total revenue generated by summing up the **revenue** from all the records.
// MAGIC 4. Implement a method **getCategoryRevenue**, which takes the **name of the category** and returns total revenue generated by the category.

// COMMAND ----------

//ANSWERS

class CompanyPerformance(val _catRecs: Array[CategoryQuarterlyRecord]){
  
  // class level variable to store the good records
  def CategoryRecs = _catRecs
  
  // calculates and returns the total revenue across all quarters and categories
  def getTotalRevenue() = CategoryRecs.map(rec => rec.revenue).reduceLeft(_ + _)
  
  // calculates and returns total revenue by the cateogry (passed as argument to the function)
  def getCategoryRevenue(_category: String): Float = {
    
    // filter records by category, extract revenue column and sum up
    CategoryRecs.filter(rec => rec.category == _category).map(rec => rec.revenue).reduceLeft(_ + _)
    
  }
  
}

// COMMAND ----------

// MAGIC %md
// MAGIC 
// MAGIC ### ![Spark Logo Tiny](https://files.training.databricks.com/images/105/logo_spark_tiny.png) Step 5(b): Initialize the class and print the insights
// MAGIC 
// MAGIC 1. Create an instance of class **CompanyPerformance** by passing **categoryRecs** in the constructor.
// MAGIC 2. Assig the instance to a variable named **companyPerf2018**
// MAGIC 3. Invoke **getTotalRevenue** and store the result in a variable named **totalRevenue**.
// MAGIC 4. Invoke **getCategoryRevenue** with argument **Exercise_Fitness** and store the result in a variable named **totalRevenueInCategory**.

// COMMAND ----------

// ANSWERS

val companyPerf2018 = new CompanyPerformance(categoryRecs)
val totalRevenue = companyPerf2018.getTotalRevenue()
val totalRevenueInCategory = companyPerf2018.getCategoryRevenue("Exercise_Fitness")

// COMMAND ----------

//TEST - Run this cell to test your solution.

var totalRevenueExpected = 57.45f
var totalCategoryRevenueExpected = 24.98f

val totalRevenueRounded = BigDecimal(totalRevenue).setScale(2, BigDecimal.RoundingMode.HALF_UP).toFloat

// rounding off the results to two decimal places for comparison
assert (totalRevenueRounded == totalRevenueExpected, s"Expected the result to be ${totalRevenueExpected} but found ${totalRevenueRounded}")
assert (totalRevenueInCategory == totalCategoryRevenueExpected, s"Expected the result to be ${totalCategoryRevenueExpected} but found ${totalRevenueInCategory}")

// COMMAND ----------

// MAGIC %md
// MAGIC 
// MAGIC Congratulation! You have completed your capstone project.

// COMMAND ----------

// MAGIC %md-sandbox
// MAGIC &copy; 2020 Databricks, Inc. All rights reserved.<br/>
// MAGIC Apache, Apache Spark, Spark and the Spark logo are trademarks of the <a href="http://www.apache.org/">Apache Software Foundation</a>.<br/>
// MAGIC <br/>
// MAGIC <a href="https://databricks.com/privacy-policy">Privacy Policy</a> | <a href="https://databricks.com/terms-of-use">Terms of Use</a> | <a href="http://help.databricks.com/">Support</a>
