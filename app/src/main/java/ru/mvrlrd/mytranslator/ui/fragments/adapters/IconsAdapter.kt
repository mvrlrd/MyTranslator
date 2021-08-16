package ru.mvrlrd.mytranslator.ui.fragments.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import kotlinx.android.synthetic.main.item_icon.view.*
import ru.mvrlrd.mytranslator.R
import ru.mvrlrd.mytranslator.data.local.entity.CategoryIconItem
import kotlin.properties.Delegates

private const val TAG = "IconsAdapter"

class IconsAdapter(private val listener: IconAdapterListener) :
    RecyclerView.Adapter<IconsAdapter.IconHolder>() {
     var icons = listOf(
        CategoryIconItem(R.drawable.addressbook_96),
        CategoryIconItem(R.drawable.airport_96),
        CategoryIconItem(R.drawable.alligator_96),
        CategoryIconItem(R.drawable.alps_96),
        CategoryIconItem(R.drawable.ambulance_96),
        CategoryIconItem(R.drawable.android_96),
        CategoryIconItem(R.drawable.aperture_96),
        CategoryIconItem(R.drawable.aquarium_96),
        CategoryIconItem(R.drawable.bam_96),
        CategoryIconItem(R.drawable.bank_96),
        CategoryIconItem(R.drawable.bowling_ball_96),
        CategoryIconItem(R.drawable.brainstorm_skill_96),
        CategoryIconItem(R.drawable.broccoli_96),
        CategoryIconItem(R.drawable.bug_96),
        CategoryIconItem(R.drawable.business_96),
        CategoryIconItem(R.drawable.cafe_96),
        CategoryIconItem(R.drawable.camper_96),
        CategoryIconItem(R.drawable.car_crash_96),
        CategoryIconItem(R.drawable.cat_96),
        CategoryIconItem(R.drawable.champagne_96),
        CategoryIconItem(R.drawable.cloud_lightning_96),
        CategoryIconItem(R.drawable.coffee_to_go_96),
        CategoryIconItem(R.drawable.console_96),
        CategoryIconItem(R.drawable.documentary_96),
        CategoryIconItem(R.drawable.elbow_96),
        CategoryIconItem(R.drawable.energy_drink_96),
        CategoryIconItem(R.drawable.english_mustache_96),
        CategoryIconItem(R.drawable.fantasy_96),
        CategoryIconItem(R.drawable.fighter_jet_96),
        CategoryIconItem(R.drawable.flower_bouquet_96),
        CategoryIconItem(R.drawable.french_fries_96),
        CategoryIconItem(R.drawable.ghost_96),
        CategoryIconItem(R.drawable.hanger_96),
        CategoryIconItem(R.drawable.weak_person_96),
        CategoryIconItem(R.drawable.water_transportation_96),
        CategoryIconItem(R.drawable.walter_white_96),
        CategoryIconItem(R.drawable.ullet_camera_96),
        CategoryIconItem(R.drawable.terrorists_96),
        CategoryIconItem(R.drawable.tent_96),
        CategoryIconItem(R.drawable.superstar_96),
        CategoryIconItem(R.drawable.sport_96),
        CategoryIconItem(R.drawable.soccer_ball_96),
        CategoryIconItem(R.drawable.smoking_96),
        CategoryIconItem(R.drawable.waste_separation_96),
        CategoryIconItem(R.drawable.nature_care_96),
        CategoryIconItem(R.drawable.large_tree_96),
        CategoryIconItem(R.drawable.futurama_bender_96)
    )

    // This keeps track of the currently selected position
     var selectedPosition by Delegates.observable(0) { property, oldPos, newPos ->
        if (newPos in icons.indices) {
            notifyItemChanged(oldPos)
            notifyItemChanged(newPos)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_icon, parent, false)
        return IconHolder(view)
    }

    override fun onBindViewHolder(holder: IconHolder, position: Int) {
        if (position in icons.indices){
            holder.bind(icons[position], position == selectedPosition)
            holder.itemView.setOnClickListener {
                listener.onIconSelected(holder.itemView,icons[position].drawableId)
                selectedPosition = position }
        }
    }

    override fun getItemCount() = icons.size



    class IconHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(iconItem: CategoryIconItem, selected: Boolean) {
                itemView.iconImageView.load(iconItem.drawableId)
                itemView.iconImageView.isSelected = selected
        }
    }

    interface IconAdapterListener {
        fun onIconSelected(view: View, iconId: Int)
    }
}