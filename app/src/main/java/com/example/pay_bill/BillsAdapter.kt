package com.example.pay_bill

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.fatura.R

class BillsAdapter(var mCtx: Context,var billList: List<Bills>) :
                     ArrayAdapter<Bills>(mCtx, R.layout.bill_detay,billList)
{
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater:LayoutInflater=LayoutInflater.from(mCtx)
        val view :View = inflater.inflate(R.layout.bill_detay,parent,false)

        val txtMonth :TextView = view.findViewById(R.id.txtMonth)
        val txtDebt :TextView = view.findViewById(R.id.txtDebt)

        txtMonth.text=billList[position].month
        txtDebt.text=billList[position].debt.toString()


        return view
    } }

