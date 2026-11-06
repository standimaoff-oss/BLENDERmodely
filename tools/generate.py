"""Runs every Octo Arms asset generator.

Usage:  python3 tools/generate.py
Regenerates textures, models, blockstates, tags, recipes, lang files and the
generated Java model spec.  All output lands inside src/main/resources (and one
generated Java file), so a plain `./gradlew build` afterwards is enough.
"""

import os
import sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

import gen_items      # noqa: E402
import gen_blocks     # noqa: E402
import gen_armor      # noqa: E402
import gen_entity     # noqa: E402
import gen_data       # noqa: E402


def main():
    print("== Octo Arms asset generation ==")
    gen_items.generate()
    gen_blocks.generate()
    gen_armor.generate()
    gen_entity.generate()
    gen_data.generate()
    print("== done ==")


if __name__ == "__main__":
    main()
