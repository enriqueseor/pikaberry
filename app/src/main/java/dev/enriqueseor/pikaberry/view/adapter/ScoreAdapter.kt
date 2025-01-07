package dev.enriqueseor.pikaberry.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.enriqueseor.pikaberry.R

class ScoreAdapter(private val scores: List<Pair<String, Int>>) :
    RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>() {

    class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playerNameTextView: TextView = itemView.findViewById(R.id.playerNameTextView)
        val playerScoreTextView: TextView = itemView.findViewById(R.id.playerScoreTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_score, parent, false)
        return ScoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val (name, score) = scores[position]
        holder.playerNameTextView.text = name
        holder.playerScoreTextView.text = String.format(score.toString())
    }

    override fun getItemCount(): Int = scores.size
}