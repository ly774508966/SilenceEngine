/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 Sri Harsha Chilakapati
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.shc.silenceengine.math;

import com.shc.silenceengine.utils.ReusableStack;

/**
 * @author Sri Harsha Chilakapati
 */
public class Vector3
{
    public static final Vector3 ZERO   = new Vector3(0, 0, 0);
    public static final Vector3 AXIS_X = new Vector3(1, 0, 0);
    public static final Vector3 AXIS_Y = new Vector3(0, 1, 0);
    public static final Vector3 AXIS_Z = new Vector3(0, 0, 1);

    public static final Vector3 UP       = new Vector3(AXIS_Y);
    public static final Vector3 DOWN     = new Vector3(AXIS_Y).negate();
    public static final Vector3 LEFT     = new Vector3(AXIS_X).negate();
    public static final Vector3 RIGHT    = new Vector3(AXIS_X);
    public static final Vector3 FORWARD  = new Vector3(AXIS_Z).negate();
    public static final Vector3 BACKWARD = new Vector3(AXIS_Z);

    public static final ReusableStack<Vector3> REUSABLE_STACK = new ReusableStack<>(Vector3::new);

    public float x, y, z;

    public Vector3()
    {
        this(0, 0, 0);
    }

    public Vector3(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(float v)
    {
        this(v, v, v);
    }

    public Vector3(Vector2 v, float z)
    {
        this(v.x, v.y, z);
    }

    public Vector3(float x, Vector2 v)
    {
        this(x, v.x, v.y);
    }

    public Vector3(Vector3 v)
    {
        this(v.x, v.y, v.z);
    }

    public Vector3(Vector4 v)
    {
        this(v.x, v.y, v.z);
    }

    @Override
    public int hashCode()
    {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (z != +0.0f ? Float.floatToIntBits(z) : 0);
        return result;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector3 vector3 = (Vector3) o;

        return Float.compare(vector3.x, x) == 0 &&
               Float.compare(vector3.y, y) == 0 &&
               Float.compare(vector3.z, z) == 0;
    }

    @Override
    public String toString()
    {
        return "[" + x + ", " + y + ", " + z + "]";
    }

    public Vector3 add(Vector2 v, float z)
    {
        return add(v.x, v.y, z);
    }

    public Vector3 add(float x, float y, float z)
    {
        return set(this.x + x, this.y + y, this.z + z);
    }

    public Vector3 set(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;

        return this;
    }

    public Vector3 add(float x, Vector2 v)
    {
        return add(x, v.x, v.y);
    }

    public Vector3 subtract(float x, float y, float z)
    {
        return add(-x, -y, -z);
    }

    public Vector3 subtract(Vector3 v)
    {
        return add(-v.x, -v.y, -v.z);
    }

    public Vector3 subtract(Vector2 v, float z)
    {
        return add(-v.x, -v.y, z);
    }

    public Vector3 subtract(float x, Vector2 v)
    {
        return add(-x, -v.x, -v.y);
    }

    public Vector3 cross(float vx, float vy, float vz)
    {
        float x = this.x * vz - this.z * vy;
        float y = this.z * vx - this.x * vz;
        float z = this.x * vy - this.y * vx;

        return set(x, y, z);
    }

    public Vector3 copy()
    {
        return new Vector3(this);
    }

    public Vector3 cross(Vector3 v)
    {
        return cross(v.x, v.y, v.z);
    }

    public Vector3 normalize()
    {
        float l = length();

        if (l == 0 || l == 1)
            return this;

        return set(x / l, y / l, z / l);
    }

    public float length()
    {
        return (float) Math.sqrt(lengthSquared());
    }

    public float lengthSquared()
    {
        return x * x + y * y + z * z;
    }

    public Vector3 negate()
    {
        return set(-x, -y, -z);
    }

    public float dot(Vector3 v)
    {
        return dot(v.x, v.y, v.z);
    }

    public float dot(float vx, float vy, float vz)
    {
        return x * vx + y * vy + z * vz;
    }

    public float distance(float x, float y, float z)
    {
        return (float) Math.sqrt(distanceSquared(x, y, z));
    }

    public float distanceSquared(float x, float y, float z)
    {
        final float x2 = (x - this.x) * (x - this.x);
        final float y2 = (y - this.y) * (y - this.y);
        final float z2 = (z - this.z) * (z - this.z);

        return x2 + y2 + z2;
    }

    public float distance(Vector3 v)
    {
        return (float) Math.sqrt(distanceSquared(v));
    }

    public float distanceSquared(Vector3 v)
    {
        return distanceSquared(v.x, v.y, v.z);
    }

    public float distance(Vector2 v)
    {
        return (float) Math.sqrt(distanceSquared(v));
    }

    public float distanceSquared(Vector2 v)
    {
        return distanceSquared(v.x, v.y, 0);
    }

    public Vector3 rotate(Vector3 axis, float angle)
    {
        Quaternion temp = Quaternion.REUSABLE_STACK.pop();

        temp.set(axis, angle);
        temp.multiply(this, this);

        Quaternion.REUSABLE_STACK.push(temp);

        return this;
    }

    public Vector3 lerp(Vector3 target, float alpha)
    {
        Vector3 temp = Vector3.REUSABLE_STACK.pop();
        scale(1f - alpha).add(temp.set(target).scale(alpha));
        Vector3.REUSABLE_STACK.push(temp);

        return this;
    }

    public Vector3 add(Vector3 v)
    {
        return add(v.x, v.y, v.z);
    }

    public Vector3 scale(float s)
    {
        return scale(s, s, s);
    }

    public Vector3 set(Vector3 v)
    {
        return set(v.x, v.y, v.z);
    }

    public Vector3 scale(float sx, float sy, float sz)
    {
        return set(x * sx, y * sy, z * sz);
    }

    public Vector3 multiply(Matrix3 m)
    {
        float rx = x * m.get(0, 0) + y * m.get(0, 1) + z * m.get(0, 2);
        float ry = x * m.get(1, 0) + y * m.get(1, 1) + z * m.get(1, 2);
        float rz = x * m.get(2, 0) + y * m.get(2, 1) + z * m.get(2, 2);

        return set(rx, ry, rz);
    }

    public Vector3 multiply(Matrix4 m)
    {
        float rx = x * m.get(0, 0) + y * m.get(1, 0) + z * m.get(2, 0) + 1 * m.get(3, 0);
        float ry = x * m.get(0, 1) + y * m.get(1, 1) + z * m.get(2, 1) + 1 * m.get(3, 1);
        float rz = x * m.get(0, 2) + y * m.get(1, 2) + z * m.get(2, 2) + 1 * m.get(3, 2);

        return set(rx, ry, rz);
    }

    public Vector3 set(float v)
    {
        return set(v, v, v);
    }

    public Vector3 rotate(Vector3 rotation)
    {
        Quaternion temp = Quaternion.REUSABLE_STACK.pop();

        temp.set(rotation.x, rotation.y, rotation.z);
        temp.multiply(this, this);

        Quaternion.REUSABLE_STACK.push(temp);

        return this;
    }
}
