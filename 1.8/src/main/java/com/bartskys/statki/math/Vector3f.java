package com.bartskys.statki.math;

public class Vector3f {

      public float x, y, z;

      public Vector3f() {
            x = 0.0f;
            y = 0.0f;
            z = 0.0f;
      }

      public Vector3f(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
      }

      @Override
      public String toString() {
            return String.format("Vector3f{ x=%.2f, y=%.2f, z=%.2f}", x, y, z);
      }
}
