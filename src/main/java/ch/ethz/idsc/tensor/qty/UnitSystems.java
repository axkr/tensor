// code by jph
package ch.ethz.idsc.tensor.qty;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import ch.ethz.idsc.tensor.Scalar;

/** EXPERIMENTAL */
public enum UnitSystems {
  ;
  /** Example: the base units of the SI unit system are
   * "A", "cd", "K", "kg", "m", "mol", "s"
   * 
   * @param unitSystem
   * @return base units of the given unitSystem */
  public static Set<String> base(UnitSystem unitSystem) {
    return unitSystem.map().values().stream() //
        .map(QuantityUnit::of) //
        .map(Unit::map) //
        .map(Map::keySet) //
        .flatMap(Collection::stream) //
        .collect(Collectors.toSet());
  }

  /** Example: for the SI unit system, the set of known atomic units contains
   * "m", "K", "W", "kW", "s", "Hz", ...
   * 
   * @return set of all atomic units known by the unit system including those that
   * are not further convertible */
  public static Set<String> known(UnitSystem unitSystem) {
    Set<String> set = new HashSet<>();
    for (Entry<String, Scalar> entry : unitSystem.map().entrySet()) {
      set.add(entry.getKey());
      set.addAll(QuantityUnit.of(entry.getValue()).map().keySet());
    }
    return set;
  }

  /***************************************************/
  /** Examples:
   * A unit system with "min" as the default time unit:
   * <pre>
   * UnitSystems.rotate[UnitSystem.SI(), "s", "min"]
   * </pre>
   * 
   * A unit system with "Hz" as the default time unit:
   * <pre>
   * UnitSystems.rotate[UnitSystem.SI(), "s", "Hz"]
   * </pre>
   * 
   * A unit system with Newton "N" instead of "s":
   * <pre>
   * UnitSystems.rotate[UnitSystem.SI(), "s", "N"]
   * </pre>
   * 
   * @param unitSystem
   * @param prev a base unit of the given unitSystem
   * @param next not a base unit of the given unitSystem, unless next equals prev
   * @return */
  public static UnitSystem rotate(UnitSystem unitSystem, String prev, String next) {
    Scalar value = StaticHelper.conversion(unitSystem, prev, next);
    if (prev.equals(next))
      return unitSystem;
    Map<String, Scalar> map = new HashMap<>(unitSystem.map()); // copy map
    map.remove(next);
    map.put(prev, value);
    return focus(SimpleUnitSystem._from(map));
  }

  private static UnitSystem focus(UnitSystem unitSystem) {
    return SimpleUnitSystem.from(unitSystem.map().entrySet().stream() //
        .collect(Collectors.toMap(Entry::getKey, entry -> unitSystem.apply(entry.getValue())))); // strict
  }

  /***************************************************/
  public static UnitSystem join(UnitSystem u1, UnitSystem u2) {
    Map<String, Scalar> map = new HashMap<>(u1.map());
    u2.map().entrySet().stream() //
        .forEach(entry -> map.put(entry.getKey(), entry.getValue()));
    return SimpleUnitSystem.from(map);
  }

  /***************************************************/
  /** @param map
   * @return */
  public static Unit unit(Map<String, Scalar> map) {
    return UnitImpl.create(map.entrySet().stream().collect(Collectors.toMap( //
        entry -> StaticHelper.requireAtomic(entry.getKey()), //
        entry -> StaticHelper.requireNonZero(entry.getValue()), //
        (u, v) -> null, TreeMap::new)));
  }
}
