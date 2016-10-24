package org.nibor.autolink.internal;

import org.nibor.autolink.LinkSpan;
import org.nibor.autolink.LinkType;

/**
 * Scan for URLs starting from the trigger character "w", requires "www.".
 * <p>
 * Based on RFC 3986.
 */
public class WwwUrlScanner implements Scanner {

    @Override
    public LinkSpan scan(final CharSequence input, int triggerIndex, int rewindIndex) {
        int afterDot = triggerIndex + 4;
        if (afterDot >= input.length() || input.charAt(triggerIndex + 1) != 'w' || input.charAt(triggerIndex + 2) != 'w' || input.charAt(triggerIndex + 3) != '.') {
            return null;
        }

        int first = triggerIndex;
        int last = findLast(input, afterDot);
        if (last == -1) {
            return null;
        }

        return new LinkSpanImpl(LinkType.WWW, first, last + 1);
    }
    
    private int findLast(CharSequence input, int beginIndex) {
        int last = Scanners.findUrlEnd(input, beginIndex);
        
        // Make sure there is at least one dot after the first dot,
        // so www.something is not allowed, but www.something.co.uk is
        int pointer = last;
        while (--pointer > beginIndex) {
            if (input.charAt(pointer) == '.' && pointer > beginIndex) return last;
        }
        
        return -1;
    }
}
