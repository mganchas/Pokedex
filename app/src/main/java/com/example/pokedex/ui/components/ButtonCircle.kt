package com.example.pokedex.ui.components

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.example.pokedex.R
import com.example.pokedex.data.extensions.toDP
import com.example.pokedex.databinding.ButtonCircleBinding

class ButtonCircle(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs) {
    companion object
    {
        private const val SIZE_LEVEL_SMALL = 0
        private const val SIZE_LEVEL_SMALLDIUM = 1
        private const val SIZE_LEVEL_MEDIUM = 2
        private const val SIZE_LEVEL_BIG = 3

        private const val BACKCOLOR_LEVEL_WHITE = 0
        private const val BACKCOLOR_LEVEL_RED = 1
        private const val BACKCOLOR_LEVEL_DISABLED = 2
        private const val DEFAULT_BACKCOLOR_LEVEL = BACKCOLOR_LEVEL_WHITE
    }

    var image: Drawable?
        private set

    @ColorRes var imageTint: Int = 0
        private set


    var backColorLevel : Int = DEFAULT_BACKCOLOR_LEVEL
        private set

    var buttonSizeLevel : Int = SIZE_LEVEL_MEDIUM
        private set

    private var initialBackColorLevel : Int = DEFAULT_BACKCOLOR_LEVEL

    private var binding : ButtonCircleBinding

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ButtonCircle,
            0,
            0
        ).apply {
            try {
                backColorLevel = getInteger(R.styleable.ButtonCircle_backColor, DEFAULT_BACKCOLOR_LEVEL)
                image = getDrawable(R.styleable.ButtonCircle_image)
                imageTint = getResourceId(R.styleable.ButtonCircle_imageTint, ContextCompat.getColor(context, (R.color.white)))
                buttonSizeLevel = getInteger(R.styleable.ButtonCircle_size, SIZE_LEVEL_MEDIUM)
            }
            finally {
                recycle()
            }
        }

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ButtonCircleBinding.inflate(inflater, this, true)

        initContainer()
        initIcon()
        updateView()
    }

    private fun initContainer()
    {
        initialBackColorLevel = backColorLevel

        val parentLayoutAsDP = getParentLayoutSizeResource().toDP(context)
        val marginBottom = 10f.toDP(context)
        val marginDefault = 5f.toDP(context)

        binding.buttonCircleParentLayout.apply {
            layoutParams = LayoutParams(parentLayoutAsDP, parentLayoutAsDP).apply {
                bottomMargin = marginBottom
                topMargin = marginDefault
                leftMargin = marginDefault
                rightMargin = marginDefault
            }
            background.level = backColorLevel
            elevation = context.resources.getDimension(R.dimen.default_elevation)
        }
    }

    private fun initIcon()
    {
        val iconSizeAsDP = getIconSize().toDP(context)
        binding.buttonCircleIcon.apply {
            this.layoutParams = LayoutParams(iconSizeAsDP, iconSizeAsDP)
            this.setImageDrawable(image)
            ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(ContextCompat.getColor(context, imageTint)))
        }
    }

    fun setEnabled() {
        binding.buttonCircleParentLayout.background.level = initialBackColorLevel
        updateView()
    }

    fun setDisabled() {
        binding.buttonCircleParentLayout.background.level = BACKCOLOR_LEVEL_DISABLED
        updateView()
    }

    private fun updateView() {
        invalidate()
        requestLayout()
    }

    private fun getIconSize() : Float
    {
        return when(buttonSizeLevel)
        {
            SIZE_LEVEL_SMALL -> context.resources.getDimension(R.dimen.inner_small_size)
            SIZE_LEVEL_SMALLDIUM -> context.resources.getDimension(R.dimen.inner_smalldium_size)
            SIZE_LEVEL_MEDIUM -> context.resources.getDimension(R.dimen.inner_medium_size)
            SIZE_LEVEL_BIG -> context.resources.getDimension(R.dimen.inner_big_size)
            else -> context.resources.getDimension(R.dimen.inner_medium_size)
        } / resources.displayMetrics.density
    }

    private fun getParentLayoutSizeResource() : Float
    {
        return when(buttonSizeLevel)
        {
            SIZE_LEVEL_SMALL -> context.resources.getDimension(R.dimen.outer_small_size)
            SIZE_LEVEL_SMALLDIUM -> context.resources.getDimension(R.dimen.outer_smalldium_size)
            SIZE_LEVEL_MEDIUM -> context.resources.getDimension(R.dimen.outer_medium_size)
            SIZE_LEVEL_BIG -> context.resources.getDimension(R.dimen.outer_big_size)
            else -> context.resources.getDimension(R.dimen.outer_medium_size)
        } / resources.displayMetrics.density
    }
}