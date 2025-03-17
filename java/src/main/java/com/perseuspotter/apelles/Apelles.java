package com.perseuspotter.apelles;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = Apelles.MODID, version = Apelles.VERSION)
public class Apelles {

  public static final String MODID = "apelles";
  public static final String VERSION = "1.0";

  public static final Apelles instance = new Apelles();

  @EventHandler
  public void init(FMLInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(this);
  }
}
