package org.android.footd;

import java.util.List;

public class MobType {
    public enum MobAbility { Normal, Flying, Immune }
    
    String textureFileName;
    DamageType type;
    List<MobAbility> abilities;
}
