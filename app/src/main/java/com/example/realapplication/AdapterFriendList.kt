package com.example.realapplication

import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_friend.view.*

class AdapterFriendList(val user : User) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.item_friend
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        val itemView = viewHolder.itemView

        itemView.et_friend_name.text = user.name
        Picasso.get().load(user.photo).into(itemView.iv_friend_photo)

    }


}