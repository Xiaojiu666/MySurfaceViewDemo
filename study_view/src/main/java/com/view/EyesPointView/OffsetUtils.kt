package com.view.EyesPointView

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import com.google.gson.Gson
import java.io.*

/**
 * Created by GuoXu on 2020/10/28 14:12.
 */
object OffsetUtils {

    val fileName: String = "offset.json"


    fun getOffsetData(context: Context): OffsetBean {
        var offsetData: String? = null
        val offsetBean: OffsetBean
        if (!TextUtils.isEmpty(offsetData)) {
            offsetData = getOffsetData4SDCard(fileName);
        } else {
            offsetData = getOffsetData4Assets(context, fileName)
        }
        offsetBean = Gson().fromJson<OffsetBean>(offsetData, OffsetBean::class.java)
        return offsetBean
    }


    private val OFFSET_FILE_PATH = Environment.getExternalStorageDirectory().toString()

    fun getOffsetData4Assets(mContext: Context, fileName: String): String? {
        try {
            val inputReader = InputStreamReader(mContext.resources.assets.open(fileName))
            val bufReader = BufferedReader(inputReader)
            var line: String? = ""
            var Result: String? = ""
            while (bufReader.readLine().also { line = it } != null) Result += line
            return Result
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getOffsetData4SDCard(fileName: String): String? {
        val stringBuffer = StringBuffer()
        val filename = File(OFFSET_FILE_PATH + fileName)
        val reader: InputStreamReader
        try {
            reader = InputStreamReader(FileInputStream(filename))
            val br = BufferedReader(reader)
            var temp: String? = ""
            while (br.readLine().also { temp = it } != null) {
                stringBuffer.append(temp)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return stringBuffer.toString()
    }

}