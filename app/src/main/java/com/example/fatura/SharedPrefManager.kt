package com.example.fatura

import android.content.Context
import android.content.Intent

class SharedPrefManager private constructor(context: Context) {

    //this method will checker whether user is already logged in or not
    val isLoggedIn: Boolean
        get() {
            val sharedPreferences = ctx?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences?.getString(KEY_USERNAME, null) != null
        }

    //this method will give the logged in user
    val user: User
        get() {
            val sharedPreferences = ctx?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return User(
                sharedPreferences!!.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getInt(KEY_TC, -1),
                            )
        }

    val staff: Staff
        get() {
            val sharedPreferences = ctx?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return Staff(
                sharedPreferences!!.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getInt(KEY_TC, -1),
            )
        }

    val bills: Bills
        get() {
            val sharedPreferences = ctx?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return Bills(
                sharedPreferences!!.getString(KEY_ID, null),
                sharedPreferences.getString(KEY_TCC,null),
                sharedPreferences.getInt(KEY_AMOUNT, 0),
                sharedPreferences.getInt(KEY_UNIT, 0),
                sharedPreferences.getInt(KEY_DEBT, 0),
                sharedPreferences.getString(KEY_MONTH, null),

            )
        }



    init {
        ctx = context
    }

    //this method will store the user data in shared preferences
    fun userLogin(user: User) {
        val sharedPreferences = ctx?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putInt(KEY_ID, user.id)
        editor?.putString(KEY_USERNAME, user.name)
        editor?.putInt(KEY_TC, user.tc)

        editor?.apply()
    }
    fun staffLogin(staff: Staff) {
        val sharedPreferences = ctx?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putInt(KEY_ID, staff.id)
        editor?.putString(KEY_USERNAME, staff.name)
        editor?.putInt(KEY_TC, staff.tc)

        editor?.apply()
    }

    fun sendBill(bill: Bills) {
        val sharedPreferences = ctx?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString(KEY_ID, bill.id)
        editor?.putString(KEY_TCC, bill.Tc)
        editor?.putInt(KEY_AMOUNT, bill.amount)
        editor?.putInt(KEY_UNIT, bill.unit)
        editor?.putInt(KEY_DEBT, bill.debt)
        editor?.putString(KEY_MONTH, bill.month)

        editor?.apply()

    }



    //this method will logout the user
    fun logout() {
        val sharedPreferences = ctx?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.clear()
        editor?.apply()
        ctx?.startActivity(Intent(ctx, Entry::class.java))
    }




    companion object {

        private val SHARED_PREF_NAME = "volleyregisterlogin"
        private val KEY_USERNAME = "keyusername"
        private val KEY_AMOUNT = "keyamount"
        private val KEY_UNIT = "keyunit"
        private val KEY_MONTH = "keymonth"
        private val KEY_TC = "keytc"
        private val KEY_TCC = "keyTc"
        private val KEY_ID = "keyid"
        private val KEY_DEBT = "keydebt"
        private var mInstance: SharedPrefManager? = null
        private var ctx: Context? = null
        @Synchronized
        fun getInstance(context: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance = SharedPrefManager(context)
            }
            return mInstance as SharedPrefManager
        }
    }
}