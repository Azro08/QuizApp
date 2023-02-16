package com.example.quizapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.databinding.ResultViewHolderBinding
import com.example.quizapp.data.room.entity.Results

class MyRvAdapter (private val resultList : List<Results>) :  RecyclerView.Adapter<MyRvAdapter.MyViewHolder>(){

    class MyViewHolder(
        private var binding: ResultViewHolderBinding) : RecyclerView.ViewHolder(binding.root)
    {
        private var result : Results?=null
        fun bind(myResult: Results){
            binding.tktTvName.text = myResult.name
            binding.tktTvScore.text = myResult.score
            binding.tktTvDate.text = myResult.date
            result = myResult
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRvAdapter.MyViewHolder {
        val binding =  ResultViewHolderBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyRvAdapter.MyViewHolder, position: Int) {
        holder.bind(resultList[position])
    }

    override fun getItemCount(): Int {
        return resultList.size
    }

}