---
layout: post
title: "How to Jekyll and GitHub Pages - knowledge base"
date:   2022-06-16 21:07
description: How to Jekyll and GitHub Pages

categories:
- web
- tools

tags:
- web
- Intellij Idea
- IDE

---

### Resources - GitHub web development

I came across several resources while creating this blog.
List will be updated accordingly.

- Intellij Idea config for Jekyll blogging and markdown use:
[Pavel Makhov blog](https://pavelmakhov.com/2017/11/idea-for-jekyll-blogging/)
  - how to create post file template in IntelliJ Idea (tested, works)

- Official GitHub manual:
 [Schnellstart, Anfang und Fortshritt](https://docs.github.com/en/pages/quickstart)

- Quick cheet sheet on markdown, also nice [Minimal Mistakes theme](https://thinkreen.github.io/markdown/markdown-cheatsheets/)

### Simplistic theme

This website is using the base Jekyll theme - Minima (at least so far). 
Basic docs and customizing tips at [jekyllrb.com](https://jekyllrb.com/)

Minima source code can be found at GitHub:
[jekyll][jekyll-organization] /
[minima](https://github.com/jekyll/minima)

Same for Jekyll framework:
[jekyll][jekyll-organization] /
[jekyll](https://github.com/jekyll/jekyll)


[jekyll-organization]: https://github.com/jekyll

### Blogging tips

Each post should be placed in `_posts` directory. Re-build the site to see your changes. 
To rebuild, run `jekyll serve`, which launches a web server and auto-regenerates your site when a file is updated.

Jekyll requires blog post files to be named according to the following format:

`YEAR-MONTH-DAY-title.MARKUP`

Where `YEAR` is a four-digit number, `MONTH` and `DAY` are both two-digit numbers, and `MARKUP` is the file extension representing the format used in the file. After that, include the necessary front matter. Take a look at the source for this post to get an idea about how it works.

Jekyll also offers powerful support for code snippets:

{% highlight ruby %}
def print_hi(name)
puts "Hi, #{name}"
end
print_hi('Tom')
#=> prints 'Hi, Tom' to STDOUT.
{% endhighlight %}

Check out the [Jekyll docs][jekyll-docs] for more info on how to get the most out of Jekyll. File all bugs/feature requests at [Jekyll’s GitHub repo][jekyll-gh]. If you have questions, you can ask them on [Jekyll Talk][jekyll-talk].

[jekyll-docs]: https://jekyllrb.com/docs/home
[jekyll-gh]:   https://github.com/jekyll/jekyll
[jekyll-talk]: https://talk.jekyllrb.com/