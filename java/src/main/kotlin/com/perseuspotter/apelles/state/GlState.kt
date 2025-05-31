package com.perseuspotter.apelles.state

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.texture.SimpleTexture
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL31
import java.lang.reflect.Field

object GlState {
    object IHateGlStateManagerSoMuchCanItGoDie {
        private val booleanStateCurrentStateF: Field
        private val alphaStateF = GlStateManager::class.java.getDeclaredField("field_179160_a").also { it.isAccessible = true }
        private val alphaStateAlphaTestF: Field
        private val alphaStateFuncF: Field
        private val alphaStateRefF: Field
        private val lightingStateF = GlStateManager::class.java.getDeclaredField("field_179158_b").also { it.isAccessible = true }
        private val lightStateF = GlStateManager::class.java.getDeclaredField("field_179159_c").also { it.isAccessible = true }
        private val colorMaterialStateF = GlStateManager::class.java.getDeclaredField("field_179156_d").also { it.isAccessible = true }
        private val colorMaterialStateColorMaterialF: Field
        private val colorMaterialStateFaceF: Field
        private val colorMaterialStateModeF: Field
        private val blendStateF = GlStateManager::class.java.getDeclaredField("field_179157_e").also { it.isAccessible = true }
        private val blendStateBlendF: Field
        private val blendStateSrcFactorF: Field
        private val blendStateDstFactorF: Field
        private val blendStateSrcFactorAlphaF: Field
        private val blendStateDstFactorAlphaF: Field
        private val depthStateF = GlStateManager::class.java.getDeclaredField("field_179154_f").also { it.isAccessible = true }
        private val depthStateDepthStateF: Field
        private val depthStateMaskEnabledF: Field
        private val depthStateDepthFuncF: Field
        private val fogStateF = GlStateManager::class.java.getDeclaredField("field_179155_g").also { it.isAccessible = true }
        private val fogStateFogF: Field
        private val fogStateModeF: Field
        private val fogStateDensityF: Field
        private val fogStateStartF: Field
        private val fogStateEndF: Field
        private val cullStateF = GlStateManager::class.java.getDeclaredField("field_179167_h").also { it.isAccessible = true }
        private val cullStateCullFaceF: Field
        private val cullStateModeF: Field
        private val polygonOffsetStateF = GlStateManager::class.java.getDeclaredField("field_179168_i").also { it.isAccessible = true }
        private val polygonOffsetStatePolygonOffsetFillF: Field
        private val polygonOffsetStatePolygonOffsetLineF: Field
        private val polygonOffsetStateFactorF: Field
        private val polygonOffsetStateUnitsF: Field
        private val colorLogicStateF = GlStateManager::class.java.getDeclaredField("field_179165_j").also { it.isAccessible = true }
        private val colorLogicStateColorLogicOpF: Field
        private val colorLogicStateOpcodeF: Field
        private val texGenStateF = GlStateManager::class.java.getDeclaredField("field_179166_k").also { it.isAccessible = true }
        private val texGenStateSF: Field
        private val texGenStateTF: Field
        private val texGenStateRF: Field
        private val texGenStateQF: Field
        private val texGenCoordTextureGenF: Field
        private val texGenCoordCoordF: Field
        private val texGenCoordParamF: Field
        private val clearStateF = GlStateManager::class.java.getDeclaredField("field_179163_l").also { it.isAccessible = true }
        private val clearStateDepthF: Field
        private val clearStateColorF: Field
        private val clearStateMaskF: Field
        private val colorRedF: Field
        private val colorGreenF: Field
        private val colorBlueF: Field
        private val colorAlphaF: Field
        private val stencilStateF = GlStateManager::class.java.getDeclaredField("field_179164_m").also { it.isAccessible = true }
        private val stencilStateStencilFuncF: Field
        private val stencilStateClearF: Field
        private val stencilStateSFailF: Field
        private val stencilStateDpFailF: Field
        private val stencilStateDpPassF: Field
        private val stencilFuncFuncF: Field
        private val stencilFuncRefF: Field
        private val stencilFuncMaskF: Field
        private val normalizeStateF = GlStateManager::class.java.getDeclaredField("field_179161_n").also { it.isAccessible = true }
        private val activeTextureUnitF = GlStateManager::class.java.getDeclaredField("field_179162_o").also { it.isAccessible = true }
        private val textureStateF = GlStateManager::class.java.getDeclaredField("field_179174_p").also { it.isAccessible = true }
        private val textureStateTexture2DStateF: Field
        private val textureStateTextureNameF: Field
        private val activeShadeModelF = GlStateManager::class.java.getDeclaredField("field_179173_q").also { it.isAccessible = true }
        private val rescaleNormalStateF = GlStateManager::class.java.getDeclaredField("field_179172_r").also { it.isAccessible = true }
        private val colorMaskF = GlStateManager::class.java.getDeclaredField("field_179171_s").also { it.isAccessible = true }
        private val colorMaskRedF: Field
        private val colorMaskGreenF: Field
        private val colorMaskBlueF: Field
        private val colorMaskAlphaF: Field
        private val colorStateF = GlStateManager::class.java.getDeclaredField("field_179170_t").also { it.isAccessible = true }

        init {
            val alphaStateO = alphaStateF.get(null)
            val alphaStateC = alphaStateO::class.java
            alphaStateAlphaTestF = alphaStateC.getDeclaredField("field_179208_a").also { it.isAccessible = true }
            alphaStateFuncF = alphaStateC.getDeclaredField("field_179206_b").also { it.isAccessible = true }
            alphaStateRefF = alphaStateC.getDeclaredField("field_179207_c").also { it.isAccessible = true }

            booleanStateCurrentStateF = alphaStateAlphaTestF.get(alphaStateO)::class.java.getDeclaredField("field_179201_b").also { it.isAccessible = true }

            val colorMaterialStateC = colorMaterialStateF.get(null)::class.java
            colorMaterialStateColorMaterialF = colorMaterialStateC.getDeclaredField("field_179191_a").also { it.isAccessible = true }
            colorMaterialStateFaceF = colorMaterialStateC.getDeclaredField("field_179189_b").also { it.isAccessible = true }
            colorMaterialStateModeF = colorMaterialStateC.getDeclaredField("field_179190_c").also { it.isAccessible = true }

            val blendStateC = blendStateF.get(null)::class.java
            blendStateBlendF = blendStateC.getDeclaredField("field_179213_a").also { it.isAccessible = true }
            blendStateSrcFactorF = blendStateC.getDeclaredField("field_179211_b").also { it.isAccessible = true }
            blendStateDstFactorF = blendStateC.getDeclaredField("field_179212_c").also { it.isAccessible = true }
            blendStateSrcFactorAlphaF = blendStateC.getDeclaredField("field_179209_d").also { it.isAccessible = true }
            blendStateDstFactorAlphaF = blendStateC.getDeclaredField("field_179210_e").also { it.isAccessible = true }

            val depthStateC = depthStateF.get(null)::class.java
            depthStateDepthStateF = depthStateC.getDeclaredField("field_179052_a").also { it.isAccessible = true }
            depthStateMaskEnabledF = depthStateC.getDeclaredField("field_179050_b").also { it.isAccessible = true }
            depthStateDepthFuncF = depthStateC.getDeclaredField("field_179051_c").also { it.isAccessible = true }

            val fogStateC = fogStateF.get(null)::class.java
            fogStateFogF = fogStateC.getDeclaredField("field_179049_a").also { it.isAccessible = true }
            fogStateModeF = fogStateC.getDeclaredField("field_179047_b").also { it.isAccessible = true }
            fogStateDensityF = fogStateC.getDeclaredField("field_179048_c").also { it.isAccessible = true }
            fogStateStartF = fogStateC.getDeclaredField("field_179045_d").also { it.isAccessible = true }
            fogStateEndF = fogStateC.getDeclaredField("field_179046_e").also { it.isAccessible = true }

            val cullStateC = cullStateF.get(null)::class.java
            cullStateCullFaceF = cullStateC.getDeclaredField("field_179054_a").also { it.isAccessible = true }
            cullStateModeF = cullStateC.getDeclaredField("field_179053_b").also { it.isAccessible = true }

            val polygonOffsetStateC = polygonOffsetStateF.get(null)::class.java
            polygonOffsetStatePolygonOffsetFillF = polygonOffsetStateC.getDeclaredField("field_179044_a").also { it.isAccessible = true }
            polygonOffsetStatePolygonOffsetLineF = polygonOffsetStateC.getDeclaredField("field_179042_b").also { it.isAccessible = true }
            polygonOffsetStateFactorF = polygonOffsetStateC.getDeclaredField("field_179043_c").also { it.isAccessible = true }
            polygonOffsetStateUnitsF = polygonOffsetStateC.getDeclaredField("field_179041_d").also { it.isAccessible = true }

            val colorLogicStateC = colorLogicStateF.get(null)::class.java
            colorLogicStateColorLogicOpF = colorLogicStateC.getDeclaredField("field_179197_a").also { it.isAccessible = true }
            colorLogicStateOpcodeF = colorLogicStateC.getDeclaredField("field_179196_b").also { it.isAccessible = true }

            val texGenStateO = texGenStateF.get(null)
            val texGenStateC = texGenStateO::class.java
            texGenStateSF = texGenStateC.getDeclaredField("field_179064_a").also { it.isAccessible = true }
            texGenStateTF = texGenStateC.getDeclaredField("field_179062_b").also { it.isAccessible = true }
            texGenStateRF = texGenStateC.getDeclaredField("field_179063_c").also { it.isAccessible = true }
            texGenStateQF = texGenStateC.getDeclaredField("field_179061_d").also { it.isAccessible = true }

            val texGenCoordC = texGenStateSF.get(texGenStateO)::class.java
            texGenCoordTextureGenF = texGenCoordC.getDeclaredField("field_179067_a").also { it.isAccessible = true }
            texGenCoordCoordF = texGenCoordC.getDeclaredField("field_179065_b").also { it.isAccessible = true }
            texGenCoordParamF = texGenCoordC.getDeclaredField("field_179066_c").also { it.isAccessible = true }

            val clearStateO = clearStateF.get(null)
            val clearStateC = clearStateO::class.java
            clearStateDepthF = clearStateC.getDeclaredField("field_179205_a").also { it.isAccessible = true }
            clearStateColorF = clearStateC.getDeclaredField("field_179203_b").also { it.isAccessible = true }
            clearStateMaskF = clearStateC.getDeclaredField("field_179204_c").also { it.isAccessible = true }

            val colorC = clearStateColorF.get(clearStateO)::class.java
            colorRedF = colorC.getDeclaredField("field_179195_a").also { it.isAccessible = true }
            colorGreenF = colorC.getDeclaredField("field_179193_b").also { it.isAccessible = true }
            colorBlueF = colorC.getDeclaredField("field_179194_c").also { it.isAccessible = true }
            colorAlphaF = colorC.getDeclaredField("field_179192_d").also { it.isAccessible = true }

            val stencilStateO = stencilStateF.get(null)
            val stencilStateC = stencilStateO::class.java
            stencilStateStencilFuncF = stencilStateC.getDeclaredField("field_179078_a").also { it.isAccessible = true }
            stencilStateClearF = stencilStateC.getDeclaredField("field_179076_b").also { it.isAccessible = true }
            stencilStateSFailF = stencilStateC.getDeclaredField("field_179077_c").also { it.isAccessible = true }
            stencilStateDpFailF = stencilStateC.getDeclaredField("field_179074_d").also { it.isAccessible = true }
            stencilStateDpPassF = stencilStateC.getDeclaredField("field_179075_e").also { it.isAccessible = true }

            val stencilFuncC = stencilStateStencilFuncF.get(stencilStateO)::class.java
            stencilFuncFuncF = stencilFuncC.getDeclaredField("field_179081_a").also { it.isAccessible = true }
            stencilFuncRefF = stencilFuncC.getDeclaredField("field_179079_b").also { it.isAccessible = true }
            stencilFuncMaskF = stencilFuncC.getDeclaredField("field_179080_c").also { it.isAccessible = true }

            val textureStateC = (textureStateF.get(null) as Array<*>)[0]!!::class.java
            textureStateTexture2DStateF = textureStateC.getDeclaredField("field_179060_a").also { it.isAccessible = true }
            textureStateTextureNameF = textureStateC.getDeclaredField("field_179059_b").also { it.isAccessible = true }

            val colorMaskC = colorMaskF.get(null)::class.java
            colorMaskRedF = colorMaskC.getDeclaredField("field_179188_a").also { it.isAccessible = true }
            colorMaskGreenF = colorMaskC.getDeclaredField("field_179186_b").also { it.isAccessible = true }
            colorMaskBlueF = colorMaskC.getDeclaredField("field_179187_c").also { it.isAccessible = true }
            colorMaskAlphaF = colorMaskC.getDeclaredField("field_179185_d").also { it.isAccessible = true }
        }

        private var alphaStateAlphaTest = false
        private var alphaStateFunc = 0
        private var alphaStateRef = 0f
        private var lightingState = false
        private val lightState = BooleanArray(8)
        private var colorMaterialStateColorMaterial = false
        private var colorMaterialStateFace = 0
        private var colorMaterialStateMode = 0
        private var blendStateBlend = false
        private var blendStateSrcFactor = 0
        private var blendStateDstFactor = 0
        private var blendStateSrcFactorAlpha = 0
        private var blendStateDstFactorAlpha = 0
        private var depthStateDepthState = false
        private var depthStateMaskEnabled = false
        private var depthStateDepthFunc = 0
        private var fogStateFog = false
        private var fogStateMode = 0
        private var fogStateDensity = 0f
        private var fogStateStart = 0f
        private var fogStateEnd = 0f
        private var cullStateCullFace = false
        private var cullStateMode = 0
        private var polygonOffsetStatePolygonOffsetFill = false
        private var polygonOffsetStatePolygonOffsetLine = false
        private var polygonOffsetStateFactor = 0f
        private var polygonOffsetStateUnits = 0f
        private var colorLogicStateColorLogicOp = false
        private var colorLogicStateOpcode = 0
        private var texGenStateSTextureGen = false
        private var texGenStateSCoord = 0
        private var texGenStateSParam = 0
        private var texGenStateTTextureGen = false
        private var texGenStateTCoord = 0
        private var texGenStateTParam = 0
        private var texGenStateRTextureGen = false
        private var texGenStateRCoord = 0
        private var texGenStateRParam = 0
        private var texGenStateQTextureGen = false
        private var texGenStateQCoord = 0
        private var texGenStateQParam = 0
        private var clearStateDepth = 0.0
        private var clearStateColorRed = 0f
        private var clearStateColorGreen = 0f
        private var clearStateColorBlue = 0f
        private var clearStateColorAlpha = 0f
        private var clearStateMask = 0
        private var stencilStateStencilFuncFunc = 0
        private var stencilStateStencilFuncRef = 0
        private var stencilStateStencilFuncMask = 0
        private var stencilStateClear = 0
        private var stencilStateSFail = 0
        private var stencilStateDpFail = 0
        private var stencilStateDpPass = 0
        private var normalizeState = false
        private var activeTextureUnit = 0
        private data class TextureState(var texture2DState: Boolean, var textureName: Int)
        private val textureState = Array(32) { TextureState(false, 0) }
        private var activeShadeModel = 0
        private var rescaleNormalState = false
        private var colorMaskRed = false
        private var colorMaskGreen = false
        private var colorMaskBlue = false
        private var colorMaskAlpha = false
        private var colorStateRed = 0f
        private var colorStateGreen = 0f
        private var colorStateBlue = 0f
        private var colorStateAlpha = 0f

        fun save() {
            val alphaStateO = alphaStateF.get(null)
            alphaStateAlphaTest = booleanStateCurrentStateF.getBoolean(alphaStateAlphaTestF.get(alphaStateO))
            alphaStateFunc = alphaStateFuncF.getInt(alphaStateO)
            alphaStateRef = alphaStateRefF.getFloat(alphaStateO)

            lightingState = booleanStateCurrentStateF.getBoolean(lightingStateF.get(null))

            (lightStateF.get(null) as Array<*>).forEachIndexed { i, v -> lightState[i] = booleanStateCurrentStateF.getBoolean(v) }

            val colorMaterialStateO = colorMaterialStateF.get(null)
            colorMaterialStateColorMaterial = booleanStateCurrentStateF.getBoolean(colorMaterialStateColorMaterialF.get(colorMaterialStateO))
            colorMaterialStateFace = colorMaterialStateFaceF.getInt(colorMaterialStateO)
            colorMaterialStateMode = colorMaterialStateModeF.getInt(colorMaterialStateO)

            val blendStateO = blendStateF.get(null)
            blendStateBlend = booleanStateCurrentStateF.getBoolean(blendStateBlendF.get(blendStateO))
            blendStateSrcFactor = blendStateSrcFactorF.getInt(blendStateO)
            blendStateDstFactor = blendStateDstFactorF.getInt(blendStateO)
            blendStateSrcFactorAlpha = blendStateSrcFactorAlphaF.getInt(blendStateO)
            blendStateDstFactorAlpha = blendStateDstFactorAlphaF.getInt(blendStateO)

            val depthStateO = depthStateF.get(null)
            depthStateDepthState = booleanStateCurrentStateF.getBoolean(depthStateDepthStateF.get(depthStateO))
            depthStateMaskEnabled = depthStateMaskEnabledF.getBoolean(depthStateO)
            depthStateDepthFunc = depthStateDepthFuncF.getInt(depthStateO)

            val fogStateO = fogStateF.get(null)
            fogStateFog = booleanStateCurrentStateF.getBoolean(fogStateFogF.get(fogStateO))
            fogStateMode = fogStateModeF.getInt(fogStateO)
            fogStateDensity = fogStateDensityF.getFloat(fogStateO)
            fogStateStart = fogStateStartF.getFloat(fogStateO)
            fogStateEnd = fogStateEndF.getFloat(fogStateO)

            val cullStateO = cullStateF.get(null)
            cullStateCullFace = booleanStateCurrentStateF.getBoolean(cullStateCullFaceF.get(cullStateO))
            cullStateMode = cullStateModeF.getInt(cullStateO)

            val polygonOffsetStateO = polygonOffsetStateF.get(null)
            polygonOffsetStatePolygonOffsetFill = booleanStateCurrentStateF.getBoolean(
                polygonOffsetStatePolygonOffsetFillF.get(polygonOffsetStateO))
            polygonOffsetStatePolygonOffsetLine = booleanStateCurrentStateF.getBoolean(
                polygonOffsetStatePolygonOffsetLineF.get(polygonOffsetStateO))
            polygonOffsetStateFactor = polygonOffsetStateFactorF.getFloat(polygonOffsetStateO)
            polygonOffsetStateUnits = polygonOffsetStateUnitsF.getFloat(polygonOffsetStateO)

            val colorLogicStateO = colorLogicStateF.get(null)
            colorLogicStateColorLogicOp = booleanStateCurrentStateF.getBoolean(colorLogicStateColorLogicOpF.get(colorLogicStateO))
            colorLogicStateOpcode = colorLogicStateOpcodeF.getInt(colorLogicStateO)

            val texGenStateO = texGenStateF.get(null)
            val texGenStateSO = texGenStateSF.get(texGenStateO)
            texGenStateSTextureGen = booleanStateCurrentStateF.getBoolean(texGenCoordTextureGenF.get(texGenStateSO))
            texGenStateSCoord = texGenCoordCoordF.getInt(texGenStateSO)
            texGenStateSParam = texGenCoordParamF.getInt(texGenStateSO)
            val texGenStateTO = texGenStateTF.get(texGenStateO)
            texGenStateTTextureGen = booleanStateCurrentStateF.getBoolean(texGenCoordTextureGenF.get(texGenStateTO))
            texGenStateTCoord = texGenCoordCoordF.getInt(texGenStateTO)
            texGenStateTParam = texGenCoordParamF.getInt(texGenStateTO)
            val texGenStateRO = texGenStateRF.get(texGenStateO)
            texGenStateRTextureGen = booleanStateCurrentStateF.getBoolean(texGenCoordTextureGenF.get(texGenStateRO))
            texGenStateRCoord = texGenCoordCoordF.getInt(texGenStateRO)
            texGenStateRParam = texGenCoordParamF.getInt(texGenStateRO)
            val texGenStateQO = texGenStateQF.get(texGenStateO)
            texGenStateQTextureGen = booleanStateCurrentStateF.getBoolean(texGenCoordTextureGenF.get(texGenStateQO))
            texGenStateQCoord = texGenCoordCoordF.getInt(texGenStateQO)
            texGenStateQParam = texGenCoordParamF.getInt(texGenStateQO)

            val clearStateO = clearStateF.get(null)
            clearStateDepth = clearStateDepthF.getDouble(clearStateO)
            val clearStateColorO = clearStateColorF.get(clearStateO)
            clearStateColorRed = colorRedF.getFloat(clearStateColorO)
            clearStateColorGreen = colorGreenF.getFloat(clearStateColorO)
            clearStateColorBlue = colorBlueF.getFloat(clearStateColorO)
            clearStateColorAlpha = colorAlphaF.getFloat(clearStateColorO)
            clearStateMask = clearStateMaskF.getInt(clearStateO)

            val stencilStateO = stencilStateF.get(null)
            val stencilStateStencilFuncO = stencilStateStencilFuncF.get(stencilStateO)
            stencilStateStencilFuncFunc = stencilFuncFuncF.getInt(stencilStateStencilFuncO)
            stencilStateStencilFuncRef = stencilFuncRefF.getInt(stencilStateStencilFuncO)
            stencilStateStencilFuncMask = stencilFuncMaskF.getInt(stencilStateStencilFuncO)
            stencilStateClear = stencilStateClearF.getInt(stencilStateO)
            stencilStateSFail = stencilStateSFailF.getInt(stencilStateO)
            stencilStateDpFail = stencilStateDpFailF.getInt(stencilStateO)
            stencilStateDpPass = stencilStateDpPassF.getInt(stencilStateO)

            normalizeState = booleanStateCurrentStateF.getBoolean(normalizeStateF.get(null))

            activeTextureUnit = activeTextureUnitF.getInt(null)

            (textureStateF.get(null) as Array<*>).forEachIndexed { i, v ->
                textureState[i].texture2DState = booleanStateCurrentStateF.getBoolean(textureStateTexture2DStateF.get(v))
                textureState[i].textureName = textureStateTextureNameF.getInt(v)
            }

            activeShadeModel = activeShadeModelF.getInt(null)

            rescaleNormalState = booleanStateCurrentStateF.getBoolean(rescaleNormalStateF.get(null))

            val colorMaskO = colorMaskF.get(null)
            colorMaskRed = colorMaskRedF.getBoolean(colorMaskO)
            colorMaskGreen = colorMaskGreenF.getBoolean(colorMaskO)
            colorMaskBlue = colorMaskBlueF.getBoolean(colorMaskO)
            colorMaskAlpha = colorMaskAlphaF.getBoolean(colorMaskO)

            val colorStateO = colorStateF.get(null)
            colorStateRed = colorRedF.getFloat(colorStateO)
            colorStateGreen = colorGreenF.getFloat(colorStateO)
            colorStateBlue = colorBlueF.getFloat(colorStateO)
            colorStateAlpha = colorAlphaF.getFloat(colorStateO)
        }

        fun load() {
            val alphaStateO = alphaStateF.get(null)
            booleanStateCurrentStateF.setBoolean(alphaStateAlphaTestF.get(alphaStateO), alphaStateAlphaTest)
            alphaStateFuncF.setInt(alphaStateO, alphaStateFunc)
            alphaStateRefF.setFloat(alphaStateO, alphaStateRef)

            booleanStateCurrentStateF.setBoolean(lightingStateF.get(null), lightingState)

            (lightStateF.get(null) as Array<*>).forEachIndexed { i, v -> booleanStateCurrentStateF.setBoolean(v, lightState[i]) }

            val colorMaterialStateO = colorMaterialStateF.get(null)
            booleanStateCurrentStateF.setBoolean(colorMaterialStateColorMaterialF.get(colorMaterialStateO), colorMaterialStateColorMaterial)
            colorMaterialStateFaceF.setInt(colorMaterialStateO, colorMaterialStateFace)
            colorMaterialStateModeF.setInt(colorMaterialStateO, colorMaterialStateMode)

            val blendStateO = blendStateF.get(null)
            booleanStateCurrentStateF.setBoolean(blendStateBlendF.get(blendStateO), blendStateBlend)
            blendStateSrcFactorF.setInt(blendStateO, blendStateSrcFactor)
            blendStateDstFactorF.setInt(blendStateO, blendStateDstFactor)
            blendStateSrcFactorAlphaF.setInt(blendStateO, blendStateSrcFactorAlpha)
            blendStateDstFactorAlphaF.setInt(blendStateO, blendStateDstFactorAlpha)

            val depthStateO = depthStateF.get(null)
            booleanStateCurrentStateF.setBoolean(depthStateDepthStateF.get(depthStateO), depthStateDepthState)
            depthStateMaskEnabledF.setBoolean(depthStateO, depthStateMaskEnabled)
            depthStateDepthFuncF.setInt(depthStateO, depthStateDepthFunc)

            val fogStateO = fogStateF.get(null)
            booleanStateCurrentStateF.setBoolean(fogStateFogF.get(fogStateO), fogStateFog)
            fogStateModeF.setInt(fogStateO, fogStateMode)
            fogStateDensityF.setFloat(fogStateO, fogStateDensity)
            fogStateStartF.setFloat(fogStateO, fogStateStart)
            fogStateEndF.setFloat(fogStateO, fogStateEnd)

            val cullStateO = cullStateF.get(null)
            booleanStateCurrentStateF.setBoolean(cullStateCullFaceF.get(cullStateO), cullStateCullFace)
            cullStateModeF.setInt(cullStateO, cullStateMode)

            val polygonOffsetStateO = polygonOffsetStateF.get(null)
            booleanStateCurrentStateF.setBoolean(polygonOffsetStatePolygonOffsetFillF.get(polygonOffsetStateO), polygonOffsetStatePolygonOffsetFill)
            booleanStateCurrentStateF.setBoolean(polygonOffsetStatePolygonOffsetLineF.get(polygonOffsetStateO), polygonOffsetStatePolygonOffsetLine)
            polygonOffsetStateFactorF.setFloat(polygonOffsetStateO, polygonOffsetStateFactor)
            polygonOffsetStateUnitsF.setFloat(polygonOffsetStateO, polygonOffsetStateUnits)

            val colorLogicStateO = colorLogicStateF.get(null)
            booleanStateCurrentStateF.setBoolean(colorLogicStateColorLogicOpF.get(colorLogicStateO), colorLogicStateColorLogicOp)
            colorLogicStateOpcodeF.setInt(colorLogicStateO, colorLogicStateOpcode)

            val texGenStateO = texGenStateF.get(null)
            val texGenStateSO = texGenStateSF.get(texGenStateO)
            booleanStateCurrentStateF.setBoolean(texGenCoordTextureGenF.get(texGenStateSO), texGenStateSTextureGen)
            texGenCoordCoordF.setInt(texGenStateSO, texGenStateSCoord)
            texGenCoordParamF.setInt(texGenStateSO, texGenStateSParam)
            val texGenStateTO = texGenStateTF.get(texGenStateO)
            booleanStateCurrentStateF.setBoolean(texGenCoordTextureGenF.get(texGenStateTO), texGenStateTTextureGen)
            texGenCoordCoordF.setInt(texGenStateTO, texGenStateTCoord)
            texGenCoordParamF.setInt(texGenStateTO, texGenStateTParam)
            val texGenStateRO = texGenStateRF.get(texGenStateO)
            booleanStateCurrentStateF.setBoolean(texGenCoordTextureGenF.get(texGenStateRO), texGenStateRTextureGen)
            texGenCoordCoordF.setInt(texGenStateRO, texGenStateRCoord)
            texGenCoordParamF.setInt(texGenStateRO, texGenStateRParam)
            val texGenStateQO = texGenStateQF.get(texGenStateO)
            booleanStateCurrentStateF.setBoolean(texGenCoordTextureGenF.get(texGenStateQO), texGenStateQTextureGen)
            texGenCoordCoordF.setInt(texGenStateQO, texGenStateQCoord)
            texGenCoordParamF.setInt(texGenStateQO, texGenStateQParam)

            val clearStateO = clearStateF.get(null)
            clearStateDepthF.setDouble(clearStateO, clearStateDepth)
            val clearStateColorO = clearStateColorF.get(clearStateO)
            colorRedF.setFloat(clearStateColorO, clearStateColorRed)
            colorGreenF.setFloat(clearStateColorO, clearStateColorGreen)
            colorBlueF.setFloat(clearStateColorO, clearStateColorBlue)
            colorAlphaF.setFloat(clearStateColorO, clearStateColorAlpha)
            clearStateMaskF.setInt(clearStateO, clearStateMask)

            val stencilStateO = stencilStateF.get(null)
            val stencilStateStencilFuncO = stencilStateStencilFuncF.get(stencilStateO)
            stencilFuncFuncF.setInt(stencilStateStencilFuncO, stencilStateStencilFuncFunc)
            stencilFuncRefF.setInt(stencilStateStencilFuncO, stencilStateStencilFuncRef)
            stencilFuncMaskF.setInt(stencilStateStencilFuncO, stencilStateStencilFuncMask)
            stencilStateClearF.setInt(stencilStateO, stencilStateClear)
            stencilStateSFailF.setInt(stencilStateO, stencilStateSFail)
            stencilStateDpFailF.setInt(stencilStateO, stencilStateDpFail)
            stencilStateDpPassF.setInt(stencilStateO, stencilStateDpPass)

            booleanStateCurrentStateF.setBoolean(normalizeStateF.get(null), normalizeState)

            activeTextureUnitF.setInt(null, activeTextureUnit)

            (textureStateF.get(null) as Array<*>).forEachIndexed { i, v ->
                booleanStateCurrentStateF.setBoolean(textureStateTexture2DStateF.get(v), textureState[i].texture2DState)
                textureStateTextureNameF.setInt(v, textureState[i].textureName)
            }

            activeShadeModelF.setInt(null, activeShadeModel)

            booleanStateCurrentStateF.setBoolean(rescaleNormalStateF.get(null), rescaleNormalState)

            val colorMaskO = colorMaskF.get(null)
            colorMaskRedF.setBoolean(colorMaskO, colorMaskRed)
            colorMaskGreenF.setBoolean(colorMaskO, colorMaskGreen)
            colorMaskBlueF.setBoolean(colorMaskO, colorMaskBlue)
            colorMaskAlphaF.setBoolean(colorMaskO, colorMaskAlpha)

            val colorStateO = colorStateF.get(null)
            colorRedF.setFloat(colorStateO, colorStateRed)
            colorGreenF.setFloat(colorStateO, colorStateGreen)
            colorBlueF.setFloat(colorStateO, colorStateBlue)
            colorAlphaF.setFloat(colorStateO, colorStateAlpha)
        }
    }
    fun push() {
        glPushAttrib(GL_ALL_ATTRIB_BITS)
        IHateGlStateManagerSoMuchCanItGoDie.save()
    }

    fun pop() {
        glPopAttrib()
        IHateGlStateManagerSoMuchCanItGoDie.load()
    }

    private var prevLw: Float? = null
    fun lineWidth(lw: Float) {
        if (lw != prevLw) {
            prevLw = lw
            glLineWidth(lw)
        }
    }
    private var prevSmooth: Boolean? = null
    fun lineSmooth(smooth: Boolean) {
        if (smooth != prevSmooth) {
            prevSmooth = smooth
            if (smooth) glEnable(GL_LINE_SMOOTH)
            else glDisable(GL_LINE_SMOOTH)
        }
    }
    private var boundTexRL: ResourceLocation? = null
    private var boundTexID: Int? = null
    fun bindTexture(tex: ResourceLocation) {
        if (tex != boundTexRL) {
            val tm = Minecraft.getMinecraft().textureManager
            var itex = tm.getTexture(tex)
            // elvis operator compiles into something that doesn't work?? thanks.
            if (itex == null) {
                itex = SimpleTexture(tex)
                // loadTexture calls GlStateManager and that's scary. but surely it will only be for one frame and will be fine right?
                tm.loadTexture(tex, itex)
                // println("loading texture $tex")
            }
            bindTexture(itex.glTextureId)
            boundTexRL = tex
        }
    }
    fun bindTexture(id: Int) {
        if (id != boundTexID) {
            boundTexID = id
            boundTexRL = null
            glBindTexture(GL_TEXTURE_2D, id)
        }
    }
    private var cr: Float? = null
    private var cg: Float? = null
    private var cb: Float? = null
    private var ca: Float? = null
    fun color(r: Float, g: Float, b: Float, a: Float) {
        if (cr != r || cg != g || cb != b || ca != a) {
            glColor4f(r, g, b, a)
            cr = r
            cg = g
            cb = b
            ca = a
        }
    }
    private var depthTest: Boolean? = null
    fun setDepthTest(depth: Boolean) {
        if (depthTest != depth) {
            if (depth) glDepthFunc(GL_LEQUAL)
            else glDepthFunc(GL_ALWAYS)
            depthTest = depth
        }
    }
    private var lighting: Int? = null
    fun setLighting(light: Int) {
        if (lighting != light) {
            if (light > 0 && (lighting == null || lighting == 0)) glEnable(GL_LIGHTING)
            when (light) {
                0 -> glDisable(GL_LIGHTING)
                1 -> glShadeModel(GL_SMOOTH)
                2 -> glShadeModel(GL_FLAT)
            }
            lighting = light
        }
    }
    fun isLightingEnabled() = lighting!! > 0
    private var boundShader: Int? = null
    fun bindShader(shader: Int) {
        if (boundShader != shader) {
            GL20.glUseProgram(shader)
            boundShader = shader
        }
    }
    private var colorArray: Boolean? = null
    fun setColorArray(enabled: Boolean) {
        if (enabled != colorArray) {
            if (enabled) glEnable(GL_COLOR_ARRAY)
            else glDisable(GL_COLOR_ARRAY)
            colorArray = enabled
        }
    }
    private var normalArray: Boolean? = null
    fun setNormalArray(enabled: Boolean) {
        if (enabled != normalArray) {
            if (enabled) glEnable(GL_NORMAL_ARRAY)
            else glDisable(GL_NORMAL_ARRAY)
            normalArray = enabled
        }
    }
    private var texArray: Boolean? = null
    fun setTexArray(enabled: Boolean) {
        if (enabled != texArray) {
            if (enabled) glEnable(GL_TEXTURE_COORD_ARRAY)
            else glDisable(GL_TEXTURE_COORD_ARRAY)
            texArray = enabled
        }
    }
    private var primitiveRestart: Int? = null
    fun setPrimitiveRestart(restart: Int) {
        if (restart != primitiveRestart) {
            GL31.glPrimitiveRestartIndex(restart)
            primitiveRestart = restart
        }
    }
    private var backfaceCull: Boolean? = null
    fun setBackfaceCull(cull: Boolean) {
        if (cull != backfaceCull) {
            if (cull) glEnable(GL_CULL_FACE)
            else glDisable(GL_CULL_FACE)
            backfaceCull = cull
        }
    }

    fun reset() {
        prevLw = null
        prevSmooth = null
        boundTexRL = null
        boundTexID = null
        cr = null
        cg = null
        cb = null
        ca = null
        depthTest = null
        lighting = null
        boundShader = null
        colorArray = null
        normalArray = null
        texArray = null
        primitiveRestart = null
        backfaceCull = null
    }
}