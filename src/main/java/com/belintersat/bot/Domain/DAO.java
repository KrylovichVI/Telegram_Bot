package com.belintersat.bot.Domain;

import java.util.Collection;

/**
 * Created by KrylovichVI on 01.08.2018.
 */
public interface DAO   {
    void addFlag(Flags flag);
    void updateFlag(Flags flags);
    Flags getFlagById(long flag_id);
    Flags getFlagByName(String name);
    Collection getAllFlags();
    void deleteFlag(Flags flags);
}
