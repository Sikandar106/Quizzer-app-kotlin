package com.example.quizzer.activities.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzer.R
import com.example.quizzer.activities.activity.QuestionActivity
import com.example.quizzer.activities.models.Quiz
import com.example.quizzer.activities.utilis.ColorPicker
import com.example.quizzer.activities.utilis.IconPicker

class QuizAdapter(private val c: Context,private val quizzes: List<Quiz>) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val v = LayoutInflater.from(c).inflate(R.layout.quiz_item,parent,false)
        return QuizViewHolder(v)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.getTitle.text = quizzes[position].title
        holder.getContainer.setBackgroundColor(Color.parseColor(ColorPicker.getColor()))
        holder.getImage.setImageResource(IconPicker.getIcon())
        holder.itemView.setOnClickListener {
            Toast.makeText(c, quizzes[position].title, Toast.LENGTH_SHORT).show()
            val intent = Intent(c,QuestionActivity::class.java)
            intent.putExtra("DATE",quizzes[position].title)
            c.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return quizzes.size
    }

    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val getTitle: TextView = itemView.findViewById(R.id.textViewQuizTitle)
            val getImage: ImageView = itemView.findViewById(R.id.imageViewQuizIcon)
            val getContainer : CardView = itemView.findViewById(R.id.card_container)
    }
}