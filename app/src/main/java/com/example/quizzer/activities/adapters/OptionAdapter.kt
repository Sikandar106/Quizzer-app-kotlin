package com.example.quizzer.activities.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzer.R
import com.example.quizzer.activities.models.Question

class OptionAdapter(private val c: Context, private var question: Question) : RecyclerView.Adapter<OptionAdapter.ViewHolder>(){

    private val questionList : List<String> = listOf(question.option1,question.option2,question.option3,question.option4)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val optionItem = LayoutInflater.from(c).inflate(R.layout.option_item,parent,false)
        return ViewHolder(optionItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.options.text = questionList[position]
        holder.itemView.setOnClickListener {
            //saving items on list into user answer
            question.userAnswer = questionList[position]
            notifyDataSetChanged()
        }
        if (question.userAnswer == questionList[position]){
            //changing view on select
            holder.itemView.setBackgroundResource(R.drawable.background_option_item_selected)
        }
        else {
            holder.itemView.setBackgroundResource(R.drawable.background_option_item)
        }
    }

    override fun getItemCount(): Int {
        return questionList.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val options: TextView = itemView.findViewById(R.id.textViewOptionItem)
    }
}