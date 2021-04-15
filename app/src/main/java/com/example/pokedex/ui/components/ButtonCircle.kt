package com.example.pokedex.ui.components

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.example.pokedex.R
import com.example.pokedex.data.extensions.toDP

class ButtonCircle(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs) {
    companion object
    {
        const val SIZE_LEVEL_SMALL = 0
        const val SIZE_LEVEL_SMALLDIUM = 1
        const val SIZE_LEVEL_MEDIUM = 2
        const val SIZE_LEVEL_BIG = 3

        const val BACKCOLOR_LEVEL_PRIMARY = 0
        const val BACKCOLOR_LEVEL_SECONDARY = 1
    }

    var image: Drawable?
    @ColorRes var imageTint: Int = 0

    var backColorLevel : Int = BACKCOLOR_LEVEL_PRIMARY
    var buttonSizeLevel : Int = SIZE_LEVEL_MEDIUM

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ButtonCircle,
            0,
            0
        ).apply {
            try {
                backColorLevel = getInteger(R.styleable.ButtonCircle_backColor, BACKCOLOR_LEVEL_PRIMARY)
                image = getDrawable(R.styleable.ButtonCircle_image)
                imageTint = getResourceId(R.styleable.ButtonCircle_imageTint, ContextCompat.getColor(context, (R.color.white)))
                buttonSizeLevel = getInteger(R.styleable.ButtonCircle_size, SIZE_LEVEL_MEDIUM)
            }
            finally {
                recycle()
            }
        }

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.button_circle, this, true)

        initParentView()
        initIcon()
        updateView()
    }

    private fun initParentView()
    {
        val parentLayoutAsDP = getParentLayoutSizeResource().toDP(context)
        val marginBottom = 10f.toDP(context)
        val marginDefault = 5f.toDP(context)

        findViewById<RelativeLayout>(R.id.button_circle_parent_layout).also {
            layoutParams = LayoutParams(parentLayoutAsDP, parentLayoutAsDP).apply {
                bottomMargin = marginBottom
                topMargin = marginDefault
                leftMargin = marginDefault
                rightMargin = marginDefault
            }
            it.background.level = backColorLevel
            elevation = context.resources.getDimension(R.dimen.default_elevation)
        }
    }

    private fun initIcon()
    {
        image ?: return
        val img = image

        val iconSizeAsDP = getIconSize().toDP(context)
        findViewById<ImageView>(R.id.button_circle_icon).apply {
            layoutParams = LayoutParams(iconSizeAsDP, iconSizeAsDP)
            setImageDrawable(img)
            ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(ContextCompat.getColor(context, imageTint)))
        }
    }

    private fun updateView() {
        invalidate()
        requestLayout()
    }

    private fun getIconSize() : Float
    {
        return when(buttonSizeLevel)
        {
            SIZE_LEVEL_SMALL -> context.resources.getDimension(R.dimen.button_circle_inner_small_size)
            SIZE_LEVEL_SMALLDIUM -> context.resources.getDimension(R.dimen.button_circle_inner_smalldium_size)
            SIZE_LEVEL_MEDIUM -> context.resources.getDimension(R.dimen.button_circle_inner_medium_size)
            SIZE_LEVEL_BIG -> context.resources.getDimension(R.dimen.button_circle_inner_big_size)
            else -> context.resources.getDimension(R.dimen.button_circle_inner_medium_size)
        } / resources.displayMetrics.density
    }

    private fun getParentLayoutSizeResource() : Float
    {
        return when(buttonSizeLevel)
        {
            SIZE_LEVEL_SMALL -> context.resources.getDimension(R.dimen.button_circle_outer_small_size)
            SIZE_LEVEL_SMALLDIUM -> context.resources.getDimension(R.dimen.button_circle_outer_smalldium_size)
            SIZE_LEVEL_MEDIUM -> context.resources.getDimension(R.dimen.button_circle_outer_medium_size)
            SIZE_LEVEL_BIG -> context.resources.getDimension(R.dimen.button_circle_outer_big_size)
            else -> context.resources.getDimension(R.dimen.button_circle_outer_medium_size)
        } / resources.displayMetrics.density
    }
}