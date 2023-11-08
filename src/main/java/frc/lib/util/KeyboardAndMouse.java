package frc.lib.util;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.networktables.NetworkTableEvent;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * Access keyboard and mouse from dashboard
 */
public class KeyboardAndMouse {

    private static KeyboardAndMouse keyboard = new KeyboardAndMouse();
    private double deltaX = 0, deltaY = 0;
    private double x = 0, y = 0;

    private KeyboardAndMouse() {
        NetworkTableInstance instance = NetworkTableInstance.getDefault();
        instance.addListener(instance.getTopic("/input/keyboardDown"),
            EnumSet.of(NetworkTableEvent.Kind.kValueRemote), (ev) -> {
                String s = ev.valueData.value.getString();
                if (s.isEmpty()) { // an empty value is sent to allow repeat values
                    return;
                }
                this.keys.put(s, true);
            });
        instance.addListener(instance.getTopic("/input/keyboardUp"),
            EnumSet.of(NetworkTableEvent.Kind.kValueRemote), (ev) -> {
                String s = ev.valueData.value.getString();
                if (s.isEmpty()) { // an empty value is sent to allow repeat values
                    return;
                }
                this.keys.put(s, false);
            });
        instance.addListener(instance.getTopic("/input/mouseDelta"),
            EnumSet.of(NetworkTableEvent.Kind.kValueRemote), (ev) -> {
                double[] s = ev.valueData.value.getDoubleArray();
                if (s.length != 2) { // an empty value is sent to allow repeat values
                    return;
                }
                this.deltaX += s[0];
                this.deltaY += s[1];
            });
    }

    public static KeyboardAndMouse getInstance() {
        return keyboard;
    }

    private Map<String, Boolean> keys = new HashMap<>();
    private Map<String, LowPassKey> lpKeys = new HashMap<>();

    /**
     *
     * @param name key name (lowercase from javascript)
     * @return keypress trigger
     */
    public Trigger key(String name) {
        return new Trigger(() -> {
            return this.keys.getOrDefault(name, false);
        });
    }

    /**
     *
     * @param button mouse button (from javascript). Usually left click is 0, right click is 2.
     * @return mousepress trigger
     */
    public Trigger mouse(int button) {
        return key("mouse" + button);
    }

    /**
     *
     * @param name key name (lowercase from javascript)
     * @return lowpasskey associated with the key
     */
    public LowPassKey lowPassKey(String name) {
        lpKeys.putIfAbsent(name, new LowPassKey(key(name)));
        return lpKeys.get(name);
    }

    /**
     *
     * @param up w
     * @param left a
     * @param down s
     * @param right d
     * @return balanced lowpasskey WASD object
     */
    public WASD wasd(String up, String left, String down, String right) {
        return new WASD(lowPassKey(up), lowPassKey(left), lowPassKey(down), lowPassKey(right));
    }

    /**
     *
     * @return change in mouse X between last loop and this one
     */
    public double getX() {
        return this.x;
    }

    /**
     *
     * @return change in mouse Y between last loop and this one
     */
    public double getY() {
        return this.y;
    }

    /**
     * update lowpasskeys and mouse
     */
    public void update() {
        for (var v : this.lpKeys.values()) {
            v.update();
        }
        resetMouse();
    }

    private void resetMouse() {
        this.x = deltaX;
        this.y = deltaY;
        this.deltaX = 0;
        this.deltaY = 0;
    }

    /**
     * Key value that interpolates between 0.0 and 1.0 over 25 loops
     */
    public static class LowPassKey {

        Trigger trigger;
        double currentValue;

        private static double filterConstant = 1.0 / 25.0;

        private LowPassKey(Trigger trigger) {
            this.trigger = trigger;
            this.currentValue = 0.0;
        }

        /**
         *
         * @return interpolated value
         */
        public double get() {
            return this.currentValue;
        }

        private void update() {
            double newValue = 0.0;
            if (trigger.getAsBoolean()) {
                newValue = 1.0;
            }
            double dir = newValue - this.currentValue;
            this.currentValue = MathUtil.clamp(
                this.currentValue + Math.signum(dir) * Math.min(Math.abs(dir), filterConstant), 0.0,
                1.0);
        }

    }

    /**
     * Simulate an axis using 4 buttons
     */
    public static class WASD {
        LowPassKey left, right, up, down;

        private WASD(LowPassKey up, LowPassKey left, LowPassKey down, LowPassKey right) {
            this.left = left;
            this.right = right;
            this.up = up;
            this.down = down;
        }

        /**
         *
         * @return interpolated value
         */
        public double getX() {
            return right.get() - left.get();
        }

        /**
         *
         * @return interpolated value
         */
        public double getY() {
            return up.get() - down.get();
        }
    }

}
