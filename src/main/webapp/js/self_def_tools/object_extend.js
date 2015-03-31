Object.defineProperty(Object.prototype,
    "extend",                  // Define Object.prototype.extend
    {
        writable: true,
        enumerable: false,     // Make it nonenumerable
        configurable: true,
        value: function(o) {   // Its value is this function
            // Get all own props, even nonenumerable ones
            var names = Object.getOwnPropertyNames(o);
            // Loop through them
            for(var i = 0; i < names.length; i++) {
                // Skip props already in this object
                if (names[i] in this) continue;
                // Get property description from o
                var desc = Object.getOwnPropertyDescriptor(o,names[i]);
                // Use it to create property on this
                Object.defineProperty(this, names[i], desc);
            }
        }
    });