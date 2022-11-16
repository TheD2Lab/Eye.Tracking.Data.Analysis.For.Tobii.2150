/*
 * Copyright (c) 2013, Bo Fu 
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
 
This analysis tool is compatible with Tobii 2150  eye tracker and the ClearView 2.7.1. 

Inputs: FXD; EVD; GZD data. 

Outputs (validated results):

1. baseline analysis including
- average pupil size of left eye; 
- average pupil size of right eye; 
- average pupil size of both eyes.

2.  FXD analysis including: 
- total number of fixations; 
- sum of all fixation duration; 
- mean duration; 
- median duration;
- StDev of durations; 
- Min. duration;
- Max. duration;  
- total number of saccades; 
- sum of all saccade length; 
- mean saccade length;
- median saccade length; 
- StDev of saccade lengths;
- min saccade length; 
- max saccade length; 
- sum of all saccade durations;
- mean saccade duration;
- median saccade duration; 
- StDev of saccade durations; 
- Min. saccade duration;
- Max. saccade duration; 
- scanpath duration; 
- fixation to saccade ratio; 
- sum of all absolute degrees; 
- mean absolute degree; 
- median absolute degree; 
- StDev of absolute degrees; 
- min absolute degree; 
- max absolute degree; 
- sum of all relative degrees; 
- mean relative degree; 
- median relative degree; 
- StDev of relative degrees; 
- min relative degree; 
- max relative degree; 
- convex hull area. 

3. EVD analysis including: 
- total number of L mouse clicks. 

4. GZD analysis including: 
- average pupil size of left eye;
- average pupil size of right eye;
- average pupil size of both eyes.